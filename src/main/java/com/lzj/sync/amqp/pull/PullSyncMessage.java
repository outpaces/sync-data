package com.lzj.sync.amqp.pull;

import com.google.gson.Gson;
import com.lzj.sync.amqp.message.BizMessage;
import com.lzj.sync.handle.OrderIncrementHandle;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 采用拉模式获取mq消息
 *
 * @author outpaces
 * @date 2019/11/10 15:10
 * @version 1.0
 */
@Slf4j
@Component(value = "mqSyncMessagePull")
public class PullSyncMessage {
    private static final long MIN_SLEEP_TIME = 10;
    private static final long MAX_SLEEP_TIME = 60000;
    private static final long STEP_SLEEP_TIME = 10;
    private static final String CONNECTION_FACTORY = "consumeIncrementConnectionFactory";
    private static final String QUEUE_INCREMENT_TASK = "queue_order_consume_increment";

    @Autowired
    OrderIncrementHandle orderIncrementHandle;

    @Autowired
    Gson gson;

    public void pullMessage(ApplicationContext context) {
        Connection connection = null;
        Channel channel = null;
        try {
            CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean(CONNECTION_FACTORY);
            connection = connectionFactory.createConnection();
            channel = connection.createChannel(false);
            channel.basicQos(0, 1, true);
            GetResponse response;
            long sleepTime = 0;

            while (orderIncrementHandle.isHandle()) {
                response = channel.basicGet(QUEUE_INCREMENT_TASK, false);
                //response == null，表示队列里没有数据；因此，先休眠，等待下一次获取
                if (response == null) {
                    if (sleepTime + STEP_SLEEP_TIME >= MAX_SLEEP_TIME) {
                        sleepTime = MIN_SLEEP_TIME;
                    } else {
                        sleepTime = sleepTime + STEP_SLEEP_TIME;
                    }
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } else {
                    MessageProperties messageProperties = getMessageProperties(response.getProps());
                    org.springframework.amqp.core.Message message = new Message(response.getBody(), messageProperties);
                    orderIncrementHandle.onMessage(toBizMessage(message), response.getEnvelope().getDeliveryTag(), channel);
                }
            }

            log.info("监听mq消息中断，有处理不了的消息");
        } catch (IOException e) {
            log.info("监听mq消息中断，rabbitmq(factory,connection,channel)发生异常", e);
        } catch (InterruptedException e) {
            log.info("监听mq消息中断，处理线程休眠发生异常", e);
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    log.error("监听mq消息，关闭channel发生异常", e);
                } catch (TimeoutException e) {
                    log.error("监听mq消息，关闭channel发生异常", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private MessageProperties getMessageProperties(AMQP.BasicProperties basicProperties) {
        if (basicProperties != null) {
            MessageProperties properties = new MessageProperties();
            properties.setContentType(basicProperties.getContentType());
            properties.setContentEncoding(basicProperties.getContentEncoding());
            properties.setContentLength(basicProperties.getBodySize());
            properties.setCorrelationId(basicProperties.getCorrelationId());
            properties.setAppId(basicProperties.getAppId());
            properties.setClusterId(basicProperties.getClusterId());
            properties.setMessageId(basicProperties.getMessageId());
            properties.setPriority(basicProperties.getPriority());
            properties.setExpiration(basicProperties.getExpiration());
            properties.setUserId(basicProperties.getUserId());
            properties.setType(basicProperties.getType());
            properties.setReplyTo(basicProperties.getReplyTo());
            if (!CollectionUtils.isEmpty(basicProperties.getHeaders())) {
                basicProperties.getHeaders().forEach((k, v) -> {
                    properties.setHeader(k, v);
                });
            }
            properties.setDeliveryMode(MessageDeliveryMode.fromInt(basicProperties.getDeliveryMode()));
            return properties;
        }
        return null;
    }

    private BizMessage toBizMessage(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        return gson.fromJson(json, BizMessage.class);
    }
}

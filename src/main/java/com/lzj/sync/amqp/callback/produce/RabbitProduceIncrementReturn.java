package com.lzj.sync.amqp.callback.produce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 处理增量发送消息的回退
 *
 * @author outpaces
 * @date 2019/11/9 18:23
 * @version 1.0
 */
@Slf4j
@Component(value = "rabbitProduceIncrementReturn")
public class RabbitProduceIncrementReturn implements RabbitTemplate.ReturnCallback {
    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            log.info("replyCode = {}, replyText = {}, exchange = {}, routingKey = {}, message = {}", replyCode, replyText, exchange, routingKey, new String(message.getBody(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

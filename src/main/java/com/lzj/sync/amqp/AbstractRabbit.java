package com.lzj.sync.amqp;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 抽象Rabbit操作类
 *
 * @author outpaces
 * @date 2019/11/10 14:52
 * @version 1.0
 */
@Slf4j
public abstract class AbstractRabbit {
    /**
     * 回调rabbit返回消息已处理
     *
     * @param channel channel
     * @param deliverTag deliverTag
     * @param multiple 是否确认多个
     * @throws IOException
     */
    protected void callBasicAck(Channel channel, long deliverTag, boolean multiple) {
        try {
            channel.basicAck(deliverTag, multiple);
        } catch (IOException ioe) {
            log.error("接收mq消息，发送(Ack)确认es-1处理应答发送异常。deliverTag = " + deliverTag, ioe);
            try {
                channel.basicAck(deliverTag, multiple);
            } catch (IOException e) {
                log.error("接收mq消息，发送(Ack)确认es-2处理应答发送异常。deliverTag = " + deliverTag, e);
            }
        }
    }

    /**
     * 回调rabbit返回消息重新入队
     *
     * @param channel channel
     * @param deliverTag deliverTag
     * @param multiple 是否确认多个
     * @param requeue 重新入队
     */
    protected void callBasicNack(Channel channel, long deliverTag, boolean multiple, boolean requeue) {
        try {
            channel.basicNack(deliverTag, multiple, requeue);
        } catch (IOException ioe) {
            log.error("接收mq消息，发送(Nack)确认es-1处理应答发送异常。deliverTag = " + deliverTag, ioe);
            try {
                channel.basicNack(deliverTag, multiple, requeue);
            } catch (IOException e) {
                log.error("接收mq消息，发送(Nack)确认es-2处理应答发送异常。deliverTag = " + deliverTag, e);
            }
        }
    }

    /**
     * 请求broker重新发送消息
     *
     * @param channel channel
     * @param deliverTag deliverTag
     * @param requeue 重新入队
     */
    protected void callBasicRecover(Channel channel, long deliverTag, boolean requeue) {
        try {
            channel.basicRecover(requeue);
        } catch (IOException e) {
            log.error("接收mq消息，发送(Recover)确认es-1处理应答发送异常。deliverTag = " + deliverTag, e);
            try {
                channel.basicRecover(requeue);
            } catch (IOException ex) {
                log.error("接收mq消息，发送(Recover)确认es-2处理应答发送异常。deliverTag = " + deliverTag, e);
            }
        }
    }
}

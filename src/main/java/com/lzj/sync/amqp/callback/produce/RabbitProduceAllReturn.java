package com.lzj.sync.amqp.callback.produce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * rabbit全量数据生产者回退
 *
 * @author outpaces
 * @date 2019/10/26 16:17
 * @version 1.0
 */
@Component
@Slf4j
public class RabbitProduceAllReturn implements RabbitTemplate.ReturnCallback {

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

package com.lzj.sync.amqp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbit增量消息消费者配置属性
 *
 * @author outpaces
 * @date 2019/11/10 14:34
 * @version 1.0
 */
@ConfigurationProperties("rabbitmq.consume.increment")
@Component
public class RabbitConsumeIncrementProperties extends AbstractRabbitProperties {
}

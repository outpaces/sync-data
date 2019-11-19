package com.lzj.sync.amqp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbit增量消息生产者配置属性
 *
 * @author outpaces
 * @date 2019/10/26 15:41
 * @version 1.0
 */
@ConfigurationProperties("rabbitmq.produce.increment")
@Component
public class RabbitIncrementProperties extends AbstractRabbitProperties {

}

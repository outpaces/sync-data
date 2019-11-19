package com.lzj.sync.amqp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbit生成者全量消息配置属性
 *
 * @author outpaces
 * @date 2019/10/25 17:31
 * @version 1.0
 */
@ConfigurationProperties("rabbitmq.produce.all")
@Component
public class RabbitAllProperties extends AbstractRabbitProperties {

}

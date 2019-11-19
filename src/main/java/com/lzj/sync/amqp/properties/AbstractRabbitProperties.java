package com.lzj.sync.amqp.properties;

import lombok.Data;

/**
 * 抽象rabbit属性配置
 * 
 * @author outpaces
 * @date 2019/10/26 15:43
 * @version 1.0
 */
@Data
public abstract class AbstractRabbitProperties {
    /**
     * rabbit的host
     */
    protected String host;
    /**
     * rabbit的port
     */
    protected Integer port = 5672;
    /**
     * rabbit的用户名
     */
    protected String userName;
    /**
     * rabbit的密码
     */
    protected String password;
    /**
     * rabbit的virtualHost
     */
    protected String virtualHost;
    /**
     * rabbit的队列名
     */
    protected String queueName;
    /**
     * rabbit的exchange
     */
    protected String exchange;
    /**
     * rabbit的routingKey
     */
    protected String routingKey;
    /**
     * rabbit的acknowledgeMode
     */
    protected String acknowledgeMode;
    /**
     * 是否发布确认，默认true
     */
    protected Boolean publisherConfirms = true;
    /**
     * 是否发布确认返回，默认true
     */
    protected Boolean publisherReturns = true;
}

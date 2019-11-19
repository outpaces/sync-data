package com.lzj.sync.loadbalance.impl;

import com.lzj.sync.loadbalance.AbstractLoadBalance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 重新发送消息的负载均衡类
 *
 * @author outpaces
 * @version 1.0
 * @date 2019/10/28 15:35
 */
@Component
public class RecoverLoadBalance extends AbstractLoadBalance<RabbitTemplate> {
    private static final String BEAN_PREFIX = "produceRecover";

    RecoverLoadBalance() {
        super();
    }

    @Override
    public RabbitTemplate roundRobin() {
        return getRoundRobin(BEAN_PREFIX);
    }
}

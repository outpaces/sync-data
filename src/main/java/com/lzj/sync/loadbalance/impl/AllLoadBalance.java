package com.lzj.sync.loadbalance.impl;

import com.lzj.sync.loadbalance.AbstractLoadBalance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 全量消息的发送负载均衡类
 *
 * @author outpaces
 * @date 2019/11/8 17:02
 * @version 1.0
 */
@Primary
@Component
public class AllLoadBalance extends AbstractLoadBalance<RabbitTemplate> {
    private static final String BEAN_PREFIX = "produceAll";
    AllLoadBalance() {
        super();
    }

    @Override
    public RabbitTemplate roundRobin() {
        return getRoundRobin(BEAN_PREFIX);
    }
}

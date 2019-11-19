package com.lzj.sync.loadbalance.impl;

import com.lzj.sync.loadbalance.AbstractLoadBalance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 增量数据的负载均衡类
 *
 * @author outpaces
 * @date 2019/11/9 18:32
 * @version 1.0
 */
@Component
public class IncrementLoadBalance extends AbstractLoadBalance<RabbitTemplate> {
    private static final String BEAN_PREFIX = "produceIncrement";
    /**
     * 负轮限载均衡算法
     *
     * @return T
     * @author lzj
     * @date 2019/10/28 14:34
     */
    @Override
    public RabbitTemplate roundRobin() {
        return getRoundRobin(BEAN_PREFIX);
    }
}

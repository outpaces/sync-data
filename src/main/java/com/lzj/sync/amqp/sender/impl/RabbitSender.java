package com.lzj.sync.amqp.sender.impl;

import com.lzj.sync.amqp.sender.IRabbitSender;
import com.lzj.sync.entity.OrderEntity;
import com.lzj.sync.loadbalance.impl.AllLoadBalance;
import com.lzj.sync.loadbalance.impl.RecoverLoadBalance;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送到rabbitmq
 *
 * @author outpaces
 * @date 2019/11/8 16:01
 * @version 1.0
 */
@Component
public class RabbitSender implements IRabbitSender<OrderEntity> {
    @Autowired
    AllLoadBalance allLoadBalance;
    @Autowired
    RecoverLoadBalance recoverLoadBalance;

    @Override
    public boolean sendSingle(OrderEntity data) {
        try {
            allLoadBalance.roundRobin().convertAndSend(data);
        } catch (AmqpException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean resend(OrderEntity data) {
        try {
            recoverLoadBalance.roundRobin().convertAndSend(data);
        } catch (AmqpException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

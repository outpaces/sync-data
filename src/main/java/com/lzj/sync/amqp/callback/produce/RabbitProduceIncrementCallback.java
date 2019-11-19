package com.lzj.sync.amqp.callback.produce;

import com.lzj.sync.amqp.observer.IPublish;
import com.lzj.sync.amqp.observer.ISubscribe;
import com.lzj.sync.event.IncrementCallbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * rabbit增量数据生产者回调
 *
 * @author outpaces
 * @date 2019/11/8 22:30
 * @version 1.0
 */
@Slf4j
@Component(value = "rabbitProduceIncrementCallback")
public class RabbitProduceIncrementCallback implements RabbitTemplate.ConfirmCallback, IPublish {
    private final List<ISubscribe> subscribes = new ArrayList<>(1);

    /**
     * Confirmation callback.
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? "-1" : correlationData.getId();

        if (StringUtils.hasText(cause)) {
            log.warn("发送全量订单任务失败，id = {}。 原因是：{}", id, cause);
        } else if (ack) {
            log.info("发送成功，id = {}", id);
        }
        publish(ack, cause);
    }

    /**
     * 发布回调事件
     *
     * @param ack
     * @param cause
     * @return void
     * @throws
     * @author outpaces
     * @date 2019/11/15 22:07
     */
    private void publish(boolean ack, String cause) {
        for (ISubscribe subscribe : subscribes) {
            subscribe.invoke(new IncrementCallbackEvent(this, ack, cause));
        }
    }

    /**
     * 增加订阅者
     *
     * @param subscribe
     * @return
     */
    @Override
    public boolean subscribe(ISubscribe subscribe) {
        return subscribes.add(subscribe);
    }

    /**
     * 移出订阅者
     *
     * @param subscribe
     * @return
     */
    @Override
    public boolean remove(ISubscribe subscribe) {
        return subscribes.remove(subscribe);
    }
}

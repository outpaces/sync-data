package com.lzj.sync.data.increment;

import com.lzj.sync.amqp.AbstractRabbit;
import com.lzj.sync.amqp.message.BizMessage;
import com.lzj.sync.amqp.observer.ISubscribe;
import com.lzj.sync.amqp.constant.RabbitSendConst;
import com.lzj.sync.event.IncrementCallbackEvent;
import com.lzj.sync.handle.IBizHandle;
import com.lzj.sync.result.IBizResult;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 抽象处理增量mq数据
 *
 * @author outpaces
 * @date 2019/11/9 17:28
 * @version 1.0
 */
@Slf4j
public abstract class DataIncrementHandle<R extends IBizResult> extends AbstractRabbit implements ISubscribe<IncrementCallbackEvent>, IBizHandle<R ,BizMessage> {
    private CountDownLatch latch = null;

    /**
     * 处理从mq过来的消息
     *
     * @param message    业务信息
     * @param deliverTag 发送标志
     * @param channel    信道
     */
    public void onMessage(@Payload BizMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag, Channel channel) {
        latch = new CountDownLatch(1);
        if (log.isDebugEnabled()) {
            log.info(message.toString());
        }
        R result = null;
        try {
            result = handle(message);
            latch.await(RabbitSendConst.RABBIT_CALLBACK_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        if (result.getResult() == true) {
            log.info("增量信息处理成功");
            callBasicAck(channel, deliverTag, false);
        } else {
            log.info("增量信息处理失败");
            callBasicRecover(channel, deliverTag, false);
        }
    }

    /**
     * 处理业务逻辑
     *
     * @param message 消息
     * @return 操作的结果
     */
    //protected abstract R handleBiz(BizMessage message);

    /**
     * 获取业务的主键
     *
     * @param message 消息
     * @return 主键
     */
    protected Long getId(BizMessage message) {
        if (CollectionUtils.isEmpty(message.getData())) {
            throw new IllegalArgumentException("message");
        }
        if (message.getId() != null) {
            return message.getId();
        }
        return Long.valueOf((String) message.getData().get("id"));
    }

    /**
     * 事件回调方法
     *
     * @param event 增量信息回调事件
     */
    @Override
    public void invoke(IncrementCallbackEvent event) {
        if (null != event) {
            if (event.isAck() && null != latch) {
                latch.countDown();
            }
        }
    }
}

package com.lzj.sync.data.recover;

import com.lzj.sync.amqp.constant.RabbitSendConst;
import com.lzj.sync.amqp.properties.RabbitAllProperties;
import com.lzj.sync.data.AbstractEntity;
import com.lzj.sync.loadbalance.impl.RecoverLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 发送失败的数据重新发送
 *
 * @author outpaces
 * @version 1.0
 * @date 2019/10/26 16:23
 */

//@Component
@Slf4j
public class DataRecoverHandle<T extends AbstractEntity> implements Runnable {
    @Autowired
    protected RabbitAllProperties allProperties;
    @Autowired
    protected RabbitSendConst rabbitSendConst;
    @Autowired(required = false)
    protected DataSaveHandle<T> dataSaveHandle;

    @Autowired
    protected RecoverLoadBalance recoverLoadBalance;
    private boolean isSend = false;

    /**
     * 是否开始重新发送失败的信息
     *
     * @return
     */
    public boolean isSend() {
        return isSend;
    }

    /**
     * 设置重新发送失败的信息
     *
     * @param send
     * @return void
     * @throws
     * @author outpaces
     * @date 2019/11/13 21:20
     */
    public void setSend(boolean send) {
        isSend = send;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (dataSaveHandle.size() > 0 && isSend) {
                    log.info("执行重新发送信息到mq，信息数量 = {}", dataSaveHandle.size());

                    for (T dto : dataSaveHandle) {
                        if (dto != null) {
                            recoverLoadBalance.roundRobin().convertAndSend(allProperties.getExchange(), allProperties.getRoutingKey(), dto, new CorrelationData(dto.getId().toString()));
                        }
                    }
                }
            } catch (Exception e) {
                log.info("处理重发任务异常", e);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

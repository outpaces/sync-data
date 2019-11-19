package com.lzj.sync.amqp.callback.produce;

import com.lzj.sync.data.recover.DataCacheHandle;
import com.lzj.sync.data.recover.DataSaveHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * rabbit全量数据生产者回调
 *
 * @author outpaces
 * @date 2019/10/26 15:51
 * @version 1.0
 */
@Component
@Slf4j
public class RabbitProduceAllCallback implements RabbitTemplate.ConfirmCallback {
    @Autowired(required = false)
    private DataCacheHandle dataCacheHandle;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? "-1" : correlationData.getId();

        if (StringUtils.hasText(cause)) {
            log.warn("发送量数据任务失败，id = {}。 原因是：{}", id, cause);
        } else if (ack) {
            dataCacheHandle.add(Long.parseLong(id));
            log.info("发送量数据任务成功，id = {}", id);
        }
    }
}

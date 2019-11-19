package com.lzj.sync.handle;

import com.lzj.sync.amqp.message.BizMessage;
import com.lzj.sync.data.increment.DataIncrementHandle;
import com.lzj.sync.entity.OrderEntity;
import com.lzj.sync.result.IncrementResultEnum;
import com.lzj.sync.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 增量订单处理
 *
 * @author outpaces
 * @date 2019/11/9 16:20
 * @version 1.0
 */
@Slf4j
@Component
public class OrderIncrementHandle extends DataIncrementHandle<IncrementResultEnum> {
    private boolean isHandle = true;

    @Autowired
    private OrderService orderService;

    public boolean isHandle() {
        return isHandle;
    }

    @Override
    public IncrementResultEnum handle(BizMessage message) {
        if (isHandle) {
            try {
                Long id = getId(message);
                Double quantity = Double.parseDouble(message.getData().get("quantity").toString());

                OrderEntity entity = new OrderEntity(id, (String) message.getData().get("userId"), (String) message.getData().get("productCode"), quantity.intValue());
                int i = orderService.updateOrder(entity);
                if (i > 0) {
                    return IncrementResultEnum.SUCCESS;
                }
                return IncrementResultEnum.FAIL;
            } catch (Exception e) {
                log.error("处理增量数据发生异常", e);
                isHandle = false;
            }
        }

        return IncrementResultEnum.FAIL;
    }
}

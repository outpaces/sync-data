package com.lzj.sync.handle;

import com.lzj.sync.data.recover.DataClearHandle;
import com.lzj.sync.entity.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * 订单数据缓存清除处理
 *
 * @author outpaces
 * @date 2019/11/4 22:04
 * @version 1.0
 */
@Component
public class OrderClearHandle extends DataClearHandle<OrderEntity> {
}

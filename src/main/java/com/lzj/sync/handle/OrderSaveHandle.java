package com.lzj.sync.handle;

import com.lzj.sync.data.recover.DataSaveHandle;
import com.lzj.sync.entity.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * 保存订单数据
 *
 * @author outpaces
 * @date 2019/11/4 22:26
 * @version 1.0
 */
@Component
public class OrderSaveHandle extends DataSaveHandle<OrderEntity> {
}

package com.lzj.sync.handle;

import com.lzj.sync.data.recover.DataRecoverHandle;
import com.lzj.sync.entity.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * 处理订单数据重新发送
 *
 * @author outpaces
 * @date 2019/11/4 21:59
 * @version 1.0
 */
@Component
public class OrderRecoverHandle extends DataRecoverHandle<OrderEntity> {
}

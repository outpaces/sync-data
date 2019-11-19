package com.lzj.sync.handle;

import com.lzj.sync.data.recover.DataCacheHandle;
import com.lzj.sync.entity.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * 订单缓存处理
 *
 * @author outpaces
 * @date 2019/11/4 22:19
 * @version 1.0
 */
@Component
public class OrderCacheHandle extends DataCacheHandle<Long> {
}

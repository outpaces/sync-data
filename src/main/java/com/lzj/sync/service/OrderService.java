package com.lzj.sync.service;

import com.lzj.sync.dao.OrderEntityMapper;
import com.lzj.sync.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单业务处理
 *
 * @author outpaces
 * @date 2019/11/9 18:00
 * @version 1.0
 */
@Service
public class OrderService {
    @Autowired
    OrderEntityMapper orderEntityMapper;

    /**
     * 获取order
     *
     * @param id id
     * @return 订单实体
     */
    public OrderEntity getOrder(Long id){
        return orderEntityMapper.selectByPrimaryKey(id);
    }

    public int updateOrder(OrderEntity orderEntity){
        return orderEntityMapper.updateByPrimaryKeySelective(orderEntity);
    }
}

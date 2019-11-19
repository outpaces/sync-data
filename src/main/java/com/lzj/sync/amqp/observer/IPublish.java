package com.lzj.sync.amqp.observer;

/**
 * 发布事件者
 *
 * @author outpaces
 * @date 2019/11/8 22:24
 * @version 1.0
 */
public interface IPublish {
    /**
     * 增加订阅者
     *
     * @param subscribe 订阅者
     * @return 订阅是否成功
     */
    boolean subscribe(ISubscribe subscribe);

    /**
     * 移除订阅者
     *
     * @param subscribe 订阅者
     * @return 移除订阅是否成功
     */
    boolean remove(ISubscribe subscribe);
}

package com.lzj.sync.amqp.observer;

import org.springframework.context.ApplicationEvent;

/**
 * 订阅者，实现此接口，相当于实现观察者模式
 *
 * @author outpaces
 * @date 2019/11/8 22:19
 * @version 1.0
 */
public interface ISubscribe<T extends ApplicationEvent> {
    /**
     * 回调方法
     *
     * @param event 事件
     */
    void invoke(T event);
}

package com.lzj.sync.amqp.sender;

import com.lzj.sync.data.AbstractEntity;

/**
 * 具体执行发送任务
 *
 * @author outpaces
 * @date 2019/10/28 13:25
 * @version 1.0
 */
public interface IRabbitSender<T extends AbstractEntity> {
    /**
     * 发送单个消息
     *
     * @param data 发送的数据
     * @author outpaces
     * @date 2019/11/16 22:33
     * @return boolean
     */
    boolean sendSingle(T data);
    /**
     *
     *
     * @param data 发送的数据
     * @author outpaces
     * @date 2019/11/16 22:34
     * @return boolean
     */
    boolean resend(T data);
}

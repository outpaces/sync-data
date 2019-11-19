package com.lzj.sync.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 发送到mq的增量数据的回调事件
 *
 * @author outpaces
 * @date 2019/11/9 16:02
 * @version 1.0
 */
@Getter
public class IncrementCallbackEvent extends ApplicationEvent {
    private boolean ack = false;
    private String cause = null;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public IncrementCallbackEvent(Object source) {
        super(source);
    }

    public IncrementCallbackEvent(Object source, boolean ack, String cause) {
        super(source);
        this.ack = ack;
        this.cause = cause;
    }
}

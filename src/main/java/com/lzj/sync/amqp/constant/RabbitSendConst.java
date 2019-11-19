package com.lzj.sync.amqp.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 消息重发属性
 *
 * @author outpaces
 * @date 2019/10/28 13:19
 * @version 1.0
 */
@Component
@Getter
@Setter
public class RabbitSendConst {
    public static final int RABBIT_SEND_TIMEOUT = 5100;
    public static final int RABBIT_RESEND_COUNT = 2000;
    public static final long RABBIT_CALLBACK_TIMEOUT = 5200;
}

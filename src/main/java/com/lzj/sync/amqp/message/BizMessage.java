package com.lzj.sync.amqp.message;

import lombok.Data;

import java.util.Map;

/**
 * 业务信息
 *
 * @author outpaces
 * @date 2019/11/9 16:33
 * @version 1.0
 */
@Data
public class BizMessage {
    private Long id;
    private String tableName;
    private Map<String,Object> data;
}

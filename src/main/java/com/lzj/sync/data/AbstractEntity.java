package com.lzj.sync.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 实体的抽象基类
 *
 * @author outpaces
 * @date 2019/10/26 16:13
 * @version 1.0
 */
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {
    protected Long id;
}

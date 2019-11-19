package com.lzj.sync.handle;

/**
 * 处理业务
 *
 * @author outpaces
 * @date 2019/11/9 17:20
 * @version 1.0
 */
public interface IBizHandle<R, T> {
    /**
     * 处理业务逻辑
     *
     * @param bizMsg 业务消息
     * @author outpaces
     * @date 2019/11/9 17:25 
     * @return R
     */
    R handle(T bizMsg);
}

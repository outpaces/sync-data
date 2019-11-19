package com.lzj.sync.loadbalance;

/**
 * 发送消息的接口
 *
 * @author outpaces
 * @date 2019/10/28 14:33
 * @version 1.0
 */
public interface ILoadBalance<T> {
    /**
     * 负轮限载均衡算法
     *
     * @author outpace
     * @date 2019/10/28 14:34
     * @return T
     */
    T roundRobin();
}

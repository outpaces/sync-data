package com.lzj.sync.result;

/**
 * 结果枚举
 *
 * @author outpaces
 * @date 2019/11/9 17:43
 * @version 1.0
 */
public enum IncrementResultEnum implements IBizResult {
    /**
     * 成功
     */
    SUCCESS,
    /**
     * 失败
     */
    FAIL;

    @Override
    public boolean getResult() {
        if (this == SUCCESS) {
            return true;
        }
        return false;
    }
}

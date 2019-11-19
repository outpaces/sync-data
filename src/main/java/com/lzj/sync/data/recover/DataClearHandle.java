package com.lzj.sync.data.recover;

import com.lzj.sync.data.AbstractEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 清除缓存中已经回调成功的消息
 *
 * @author outpaces
 * @date 2019/10/28 13:50
 * @version 1.0
 */
@Slf4j
public class DataClearHandle<T extends AbstractEntity> implements Runnable {
    @Autowired
    protected DataSaveHandle<T> dataSaveHandle;
    @Autowired(required = false)
    protected DataCacheHandle<Long> dataCacheHandle;
    int size = 0;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        int temp = 0;
        while (true) {
            temp = 0;
            if (dataSaveHandle.size() > 0 && dataCacheHandle.size() > 0) {
                T head = null;
                while ((head = dataSaveHandle.getDataSet().peek()) != null && dataCacheHandle.size() > 0) {
                    if (dataCacheHandle.remove(head.getId())) {
                        temp++;
                        dataSaveHandle.getDataSet().poll();
                    }
                }
                size += temp;
                log.info("本次清除{}条数据", temp);
                log.info("总共清除{}条数据", size);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

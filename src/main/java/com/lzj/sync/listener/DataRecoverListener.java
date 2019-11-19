package com.lzj.sync.listener;

import com.lzj.sync.data.recover.DataClearHandle;
import com.lzj.sync.data.recover.DataRecoverHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * 数据抽象发送的监听器
 *
 * @author outpaces
 * @date 2019/10/26 16:34
 * @version 1.0
 */
@Component
@Order(2)
@Slf4j
public class DataRecoverListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("执行重发数据任务事件");
        ApplicationContext context = event.getApplicationContext();
        if (null != context) {
            DataRecoverHandle dataRecoverHandle = context.getBean(DataRecoverHandle.class);
            //DataRecoverHandle dataRecoverHandle = new DataRecoverHandle();
            DataClearHandle dataClearHandle = context.getBean(DataClearHandle.class);
            ExecutorService executors = (ExecutorService)context.getBean("dataExecutorService");
            executors.execute(dataClearHandle);
            executors.execute(dataRecoverHandle);

            log.info("开启重发任务成功");
        }
    }
}

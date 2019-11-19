package com.lzj.sync.listener;

import com.lzj.sync.amqp.observer.IPublish;
import com.lzj.sync.amqp.observer.ISubscribe;
import com.lzj.sync.amqp.pull.PullSyncMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 增量数据处理的监听器
 *
 * @author outpaces
 * @date 2019/11/8 22:37
 * @version 1.0
 */
@Slf4j
@Component
public class DataIncrementListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("执行订阅增量的RabbitMq回调事件");
        ApplicationContext context = event.getApplicationContext();
        if (null != context) {
            //配置增量处理的信息订阅
            Map<String, ISubscribe> subscribeMap = context.getBeansOfType(ISubscribe.class);
            Map<String, IPublish> publishMap = context.getBeansOfType(IPublish.class);
            if (!CollectionUtils.isEmpty(subscribeMap) && !CollectionUtils.isEmpty(publishMap)) {
                IPublish publish = publishMap.get("rabbitProduceIncrementCallback");
                if (null != publish) {
                    for (ISubscribe subscribe : subscribeMap.values()) {
                        publish.subscribe(subscribe);
                    }
                }
            } else {
                log.info("没有发现实现ISubscribe和IPublish接口的bean");
            }

            //启动增量处理
            PullSyncMessage mqSyncMessagePull = (PullSyncMessage) context.getBean("mqSyncMessagePull");
            mqSyncMessagePull.pullMessage(context);
        }
    }
}

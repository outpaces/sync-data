package com.lzj.sync.amqp.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 通用配置
 *
 * @author outpaces
 * @date 2019/11/12 18:23
 * @version 1.0
 */
@Configuration
public class GlobalConfig {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置线程池
     *
     * @return java.util.concurrent.ExecutorService
     * @author outpaces
     * @date 2019/11/16 22:10
     */
    @Bean("dataExecutorService")
    public ExecutorService getExecutors() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("data-executor-%d").build();
        return new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS
                , new LinkedBlockingQueue<Runnable>(1024), nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}

package com.lzj.sync.amqp.config.produce;

import com.lzj.sync.amqp.callback.produce.RabbitProduceAllCallback;
import com.lzj.sync.amqp.callback.produce.RabbitProduceAllReturn;
import com.lzj.sync.amqp.config.AbstractRabbitConfig;
import com.lzj.sync.amqp.properties.RabbitAllProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 消息重新发送的rabbitmq配置
 *
 * @author outpaces
 * @date 2019/10/28 14:36
 * @version 1.0
 */
@Configuration
public class ProduceRecoverConfig extends AbstractRabbitConfig {
    @Autowired
    RabbitAllProperties allProperties;
    @Autowired
    RabbitProduceAllCallback rabbitProduceAllCallback;
    @Autowired
    RabbitProduceAllReturn rabbitProduceAllReturn;
    @Autowired
    MessageConverter jsonMessageConverter;

    /**
     * rabbitMq的连接工厂
     *
     * @return
     */
    @Bean("produceRecoverConnectionFactory")
    public ConnectionFactory recoverConnectionFactory() {
        return getCacheConnectionFactory(allProperties.getHost(), allProperties.getPort(), allProperties.getPublisherConfirms(), allProperties.getPublisherReturns()
                , allProperties.getVirtualHost(), allProperties.getUserName(), allProperties.getPassword(), -1);
    }

    /**
     * rabbitMq的管理者
     *
     * @return
     */
    @Bean(name = "produceRecoverAmqpAdmin")
    public AmqpAdmin produceRecoverAmqpAdmin() {
        return new RabbitAdmin(recoverConnectionFactory());
    }

    /**
     * 关联到rabbitMq的RabbitTemplate
     *
     * @return RabbitTemplate
     */
    @Primary
    @Bean(name = "produceRecoverRabbitTemplate")
    public RabbitTemplate produceRecoverRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(recoverConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }

    /**
     * 关联到rabbitMq的Queue
     *
     * @return Queue
     */
    @Bean(name = "produceRecoverRabbitTemplate1")
    public RabbitTemplate produceRecoverRabbitTemplate1() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(recoverConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }

    /**
     * 配置RabbitTemplate
     *
     * @param rabbitTemplate rabbitTemplate
     * @param exchange exchange
     */
    private void configRabbitTemplate(RabbitTemplate rabbitTemplate, String exchange) {
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(exchange);
        //设置回调接口
        rabbitTemplate.setConfirmCallback(rabbitProduceAllCallback);
        //设置失败返回回调接口
        rabbitTemplate.setReturnCallback(rabbitProduceAllReturn);
        rabbitTemplate.setMandatory(true);
    }
}

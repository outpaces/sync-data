package com.lzj.sync.amqp.config.consume;

import com.lzj.sync.amqp.config.AbstractRabbitConfig;
import com.lzj.sync.amqp.properties.RabbitConsumeIncrementProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 增量消费，数据源是rabbitMq的配置
 *
 * @author outpaces
 * @date 2019/11/10 14:30
 * @version 1.0
 */
@Configuration
public class ConsumeIncrementConfig extends AbstractRabbitConfig {
    @Autowired
    private RabbitConsumeIncrementProperties incrementProperties;
    @Autowired
    MessageConverter jsonMessageConverter;

    /**
     * rabbitMq的连接工厂
     *
     * @return
     */
    @Bean("consumeIncrementConnectionFactory")
    public ConnectionFactory consumeIncrementConnectionFactory() {
        return getCacheConnectionFactory(incrementProperties.getHost(), incrementProperties.getPort(), incrementProperties.getPublisherConfirms(), incrementProperties.getPublisherReturns()
                , incrementProperties.getVirtualHost(), incrementProperties.getUserName(), incrementProperties.getPassword(), -1);
    }

    /**
     * rabbitMq的管理者
     * @return
     */
    @Bean(name = "consumeIncrementAmqpAdmin")
    public AmqpAdmin consumeIncrementAmqpAdmin() {
        return new RabbitAdmin(consumeIncrementConnectionFactory());
    }

    /**
     * 关联到rabbitMq的RabbitTemplate
     * @return
     */
    @Bean(name = "consumeIncrementRabbitTemplate")
    public RabbitTemplate consumeIncrementRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(consumeIncrementConnectionFactory());
        configRabbitTemplate(rabbitTemplate, incrementProperties.getExchange());

        return rabbitTemplate;
    }

    /**
     * 关联到rabbitMq的Queue
     *
     * @return Queue
     */
    @Bean(name = "consumeIncrementQueue")
    public Queue consumeIncrementQueue() {
        Queue queue = new Queue(incrementProperties.getQueueName());
        queue.setAdminsThatShouldDeclare(consumeIncrementAmqpAdmin());
        return queue;
    }

    /**
     * 采用直连方式的Exchange
     *
     * @return 直连Exchange
     */
    @Bean(name = "consumeIncrementDirectExchange")
    public DirectExchange consumeIncrementDirectExchange() {
        return new DirectExchange(incrementProperties.getExchange(), true, false);
    }

    /**
     * 将Queue和Exchange通过RoutingKey关联起来
     *
     * @return Binding
     */
    @Bean(name = "consumeIncrementDataBinding")
    public Binding consumeIncrementDataBinding() {
        return BindingBuilder.bind(consumeIncrementQueue()).to(consumeIncrementDirectExchange()).with(incrementProperties.getRoutingKey());
    }

    /**
     * 配置RabbitTemplate
     *
     * @param rabbitTemplate
     */
    private void configRabbitTemplate(RabbitTemplate rabbitTemplate, String exchange) {
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(exchange);
    }
}

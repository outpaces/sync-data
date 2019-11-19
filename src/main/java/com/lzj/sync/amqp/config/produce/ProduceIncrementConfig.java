package com.lzj.sync.amqp.config.produce;

import com.lzj.sync.amqp.callback.produce.RabbitProduceIncrementCallback;
import com.lzj.sync.amqp.callback.produce.RabbitProduceIncrementReturn;
import com.lzj.sync.amqp.config.AbstractRabbitConfig;
import com.lzj.sync.amqp.properties.RabbitIncrementProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 增量生产者，数据源是rabbitMq的配置
 *
 * @author outpaces
 * @date 2019/10/25 17:25
 * @version 1.0
 */
@Configuration
public class ProduceIncrementConfig extends AbstractRabbitConfig {
    @Autowired
    private RabbitIncrementProperties incrementProperties;
    @Autowired
    RabbitProduceIncrementCallback rabbitProduceIncrementCallback;
    @Autowired
    RabbitProduceIncrementReturn rabbitProduceIncrementReturn;
    @Autowired
    MessageConverter jsonMessageConverter;

    /**
     * rabbitMq的连接工厂
     *
     * @return 缓存连接工厂
     */
    @Bean("produceIncrementConnectionFactory")
    public ConnectionFactory produceIncrementConnectionFactory() {
        return getCacheConnectionFactory(incrementProperties.getHost(), incrementProperties.getPort(), incrementProperties.getPublisherConfirms(), incrementProperties.getPublisherReturns()
                , incrementProperties.getVirtualHost(), incrementProperties.getUserName(), incrementProperties.getPassword(), 1000);
    }

    /**
     * 消息监听器容器工厂
     *
     * @param connectionFactory 连接工厂
     * @return 容器工厂
     */
    @Bean("produceIncrementListenerContainerFactory")
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("produceIncrementConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMessageConverter(jsonMessageConverter);
        containerFactory.setAcknowledgeMode(getAcknowledgeMode(incrementProperties.getAcknowledgeMode()));

        return containerFactory;
    }

    /**
     * rabbitMq的管理者
     *
     * @return
     */
    @Bean(name = "produceIncrementAmqpAdmin")
    public AmqpAdmin produceIncrementAmqpAdmin() {
        return new RabbitAdmin(produceIncrementConnectionFactory());
    }

    /**
     * 关联到rabbitMq的RabbitTemplate
     *
     * @return RabbitTemplate
     */
    @Bean(name = "produceIncrementRabbitTemplate")
    public RabbitTemplate produceIncrementRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(produceIncrementConnectionFactory());
        configRabbitTemplate(rabbitTemplate, incrementProperties.getExchange());

        return rabbitTemplate;
    }

    /**
     * 关联到rabbitMq的Queue
     *
     * @return Queue
     */
    @Bean(name = "produceIncrementQueue")
    public Queue produceIncrementQueue() {
        Queue queue = new Queue(incrementProperties.getQueueName());
        queue.setAdminsThatShouldDeclare(produceIncrementAmqpAdmin());
        return queue;
    }

    /**
     * 采用直连方式的Exchange
     *
     * @return 直连Exchange
     */
    @Bean(name = "produceIncrementDirectExchange")
    public DirectExchange produceIncrementDirectExchange() {
        return new DirectExchange(incrementProperties.getExchange(), true, false);
    }

    /**
     * 将Queue和Exchange通过RoutingKey关联起来
     *
     * @return Binding
     */
    @Bean(name = "produceIncrementDataBinding")
    public Binding produceIncrementDataBinding() {
        return BindingBuilder.bind(produceIncrementQueue()).to(produceIncrementDirectExchange()).with(incrementProperties.getRoutingKey());
    }

    /**
     * 配置RabbitTemplate
     *
     * @param rabbitTemplate rabbitTemplate
     * @param exchange exchange
     * @author outpaces
     * @date 2019/11/16 22:01
     * @return void
     * @throws
     */
    private void configRabbitTemplate(RabbitTemplate rabbitTemplate, String exchange) {
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(exchange);
        //设置回调接口
        rabbitTemplate.setConfirmCallback(rabbitProduceIncrementCallback);
        //设置失败返回回调接口
        rabbitTemplate.setReturnCallback(rabbitProduceIncrementReturn);
        rabbitTemplate.setMandatory(true);
    }
}

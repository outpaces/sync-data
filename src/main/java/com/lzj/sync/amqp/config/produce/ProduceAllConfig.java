package com.lzj.sync.amqp.config.produce;

import com.lzj.sync.amqp.callback.produce.RabbitProduceAllCallback;
import com.lzj.sync.amqp.callback.produce.RabbitProduceAllReturn;
import com.lzj.sync.amqp.config.AbstractRabbitConfig;
import com.lzj.sync.amqp.properties.RabbitAllProperties;
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
import org.springframework.context.annotation.Primary;

/**
 * 全量消费，rabbitmq生产者配置
 *
 * @author outpaces
 * @date 2019/10/25 17:35
 * @version 1.0
 */
@Configuration
public class ProduceAllConfig extends AbstractRabbitConfig {
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
     * @return 缓存连接工厂
     */
    @Primary
    @Bean("produceAllConnectionFactory")
    public ConnectionFactory produceAllConnectionFactory() {
        return getCacheConnectionFactory(allProperties.getHost(), allProperties.getPort(), allProperties.getPublisherConfirms(), allProperties.getPublisherReturns()
                , allProperties.getVirtualHost(), allProperties.getUserName(), allProperties.getPassword(), -1);
    }

    /**
     * 消息监听器容器工厂
     *
     * @param connectionFactory 连接工厂
     * @return 容器工厂
     */
    @Primary
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("produceAllConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMessageConverter(jsonMessageConverter);
        containerFactory.setAcknowledgeMode(getAcknowledgeMode(allProperties.getAcknowledgeMode()));

        return containerFactory;
    }

    /**
     * rabbitMq的管理者
     *
     * @return 管理者
     */
    @Primary
    @Bean(name = "produceAllAmqpAdmin")
    public AmqpAdmin produceAllAmqpAdmin() {
        return new RabbitAdmin(produceAllConnectionFactory());
    }

    /**
     * 关联到rabbitMq的RabbitTemplate
     *
     * @return RabbitTemplate
     */
    @Primary
    @Bean(name = "produceAllRabbitTemplate")
    public RabbitTemplate produceAllRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(produceAllConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }
    /**
     * 关联到rabbitMq的RabbitTemplate2
     *
     * @return RabbitTemplate
     */
    @Bean(name = "produceAllRabbitTemplate2")
    public RabbitTemplate produceAllRabbitTemplate2() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(produceAllConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }
    /**
     * 关联到rabbitMq的RabbitTemplate3
     *
     * @return RabbitTemplate
     */
    @Bean(name = "produceAllRabbitTemplate3")
    public RabbitTemplate produceAllRabbitTemplate3() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(produceAllConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }
    /**
     * 关联到rabbitMq的RabbitTemplate4
     *
     * @return RabbitTemplate
     */
    @Bean(name = "produceAllRabbitTemplate4")
    public RabbitTemplate produceAllRabbitTemplate4() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(produceAllConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }
    /**
     * 关联到rabbitMq的RabbitTemplate5
     *
     * @return RabbitTemplate
     */
    @Bean(name = "produceAllRabbitTemplate5")
    public RabbitTemplate produceAllRabbitTemplate5() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(produceAllConnectionFactory());
        configRabbitTemplate(rabbitTemplate, allProperties.getExchange());

        return rabbitTemplate;
    }

    /**
     * 关联到rabbitMq的Queue
     *
     * @return Queue
     */
    @Primary
    @Bean(name = "produceAllQueue")
    public Queue produceAllQueue() {
        Queue queue = new Queue(allProperties.getQueueName());
        queue.setAdminsThatShouldDeclare(produceAllAmqpAdmin());
        return queue;
    }

    /**
     * 采用直连方式的Exchange
     *
     * @return 直连Exchange
     */
    @Primary
    @Bean(name = "produceAllDirectExchange")
    public DirectExchange produceAllDirectExchange() {
        return new DirectExchange(allProperties.getExchange(), true, false);
    }

    /**
     * 将Queue和Exchange通过RoutingKey关联起来
     *
     * @return Binding
     */
    @Primary
    @Bean(name = "produceAllDataBinding")
    public Binding produceAllDataBinding() {
        return BindingBuilder.bind(produceAllQueue()).to(produceAllDirectExchange()).with(allProperties.getRoutingKey());
    }

    /**
     * 配置RabbitTemplate
     *
     * @param rabbitTemplate rabbitTemplate
     * @param exchange exchange
     * @return void
     * @throws
     * @author outpaces
     * @date 2019/11/12 16:17
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

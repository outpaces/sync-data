package com.lzj.sync.amqp.config;

import com.lzj.sync.amqp.constant.MqAcknowledgeModeConst;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.util.StringUtils;

/**
 * Rabbit配置抽象类
 *
 * @author outpaces
 * @date 2019/11/15
 * @version 1.0
 */
public abstract class AbstractRabbitConfig {

    /**
     * 获取队列的应答模式
     *
     * @param acknowledgeMode 是MqAcknowledgeMode类中定义的字符串
     * @return org.springframework.amqp.core.AcknowledgeMode
     * @throws
     * @author outpaces
     * @date 2019/10/25 17:08
     */
    protected AcknowledgeMode getAcknowledgeMode(String acknowledgeMode) {
        if (StringUtils.hasText(acknowledgeMode)) {
            return AcknowledgeMode.AUTO;
        }
        switch (acknowledgeMode.toLowerCase()) {
            case MqAcknowledgeModeConst.MANUAL:
                return AcknowledgeMode.MANUAL;
            case MqAcknowledgeModeConst.NONE:
                return AcknowledgeMode.NONE;
            default:
                return AcknowledgeMode.AUTO;
        }
    }

    /**
     * 创建缓存连接工厂
     *
     * @param host                ip
     * @param port                端口
     * @param isPublisherConfirms 是否启用发布确认模式
     * @param isPublisherReturns  是否启用发布回退模式
     * @param virtualHost         virtualHost
     * @param userName            用户名
     * @param password            密码
     * @param channelCacheSize    缓存的channel数量
     * @return org.springframework.amqp.rabbit.connection.ConnectionFactory
     * @throws
     * @author outpaces
     * @date 2019/10/25 17:11
     */
    protected ConnectionFactory getCacheConnectionFactory(String host, int port, boolean isPublisherConfirms, boolean isPublisherReturns, String virtualHost, String userName, String password, int channelCacheSize) {
        return new ConnectionFactoryBuilder(host,port).isPublisherConfirms(isPublisherConfirms).isPublisherReturns(isPublisherReturns).virtualHost(virtualHost).userName(userName)
                .password(password).channelCacheSize(channelCacheSize).build();
    }
}

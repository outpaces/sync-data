package com.lzj.sync.amqp.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * rabbit连接工厂builder
 *
 * @author outpaces
 * @date 2019/11/15 16:55
 * @version 1.0
 */
public class ConnectionFactoryBuilder {
    private String host;
    private int port = 5672;
    private boolean isPublisherConfirms = false;
    private boolean isPublisherReturns = false;
    private String virtualHost = "/";
    private String userName = "guest";
    private String password = "guest";
    private int channelCacheSize;

    /**
     * 设置host
     *
     * @param host
     * @return
     */
    public ConnectionFactoryBuilder host(String host) {
        this.host = host;
        return this;
    }

    /**
     * 设置port
     *
     * @param port
     * @return
     */
    public ConnectionFactoryBuilder port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 设置isPublisherConfirms
     *
     * @param isPublisherConfirms
     * @return
     */
    public ConnectionFactoryBuilder isPublisherConfirms(boolean isPublisherConfirms) {
        this.isPublisherConfirms = isPublisherConfirms;
        return this;
    }

    /**
     * 设置isPublisherReturns
     *
     * @param isPublisherReturns
     * @return
     */
    public ConnectionFactoryBuilder isPublisherReturns(boolean isPublisherReturns) {
        this.isPublisherReturns = isPublisherReturns;
        return this;
    }

    /**
     * 设置virtualHost
     *
     * @param virtualHost
     * @return
     */
    public ConnectionFactoryBuilder virtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
        return this;
    }

    /**
     * 设置userName
     *
     * @param userName
     * @return
     */
    public ConnectionFactoryBuilder userName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 设置password
     *
     * @param password
     * @return
     */
    public ConnectionFactoryBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * 设置缓存的channelCacheSize
     *
     * @param channelCacheSize
     * @return
     */
    public ConnectionFactoryBuilder channelCacheSize(int channelCacheSize) {
        this.channelCacheSize = channelCacheSize;
        return this;
    }

    public ConnectionFactoryBuilder() {
    }

    public ConnectionFactoryBuilder(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 构建ConnectionFactory
     *
     * @return
     */
    public ConnectionFactory build() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setPublisherConfirms(isPublisherConfirms);
        connectionFactory.setPublisherReturns(isPublisherReturns);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        if (channelCacheSize > 0) {
            connectionFactory.setChannelCacheSize(channelCacheSize);
        }

        return connectionFactory;
    }
}

package com.lzj.sync;

import com.lzj.sync.amqp.properties.RabbitConsumeIncrementProperties;
import com.lzj.sync.dao.OrderEntityMapper;
import com.lzj.sync.data.recover.DataRecoverHandle;
import com.lzj.sync.data.recover.DataSaveHandle;
import com.lzj.sync.entity.OrderEntity;
import com.lzj.sync.loadbalance.impl.AllLoadBalance;
import com.lzj.sync.loadbalance.impl.IncrementLoadBalance;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class SyncDataApplicationTests {
    @Autowired
    private RabbitConsumeIncrementProperties incrementProperties;
    @Autowired
    OrderEntityMapper userOrderMapper;

    @Autowired
    AllLoadBalance allLoadBalance;

    @Autowired
    IncrementLoadBalance incrementLoadBalance;

    @Autowired
    DataSaveHandle<OrderEntity> dataSaveHandle;

    @Autowired
    DataRecoverHandle<OrderEntity> dataRecoverHandle;

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean("produceIncrementConnectionFactory");
        Connection connection = connectionFactory.createConnection();
    }

    @Test
    void getOrder() {
        OrderEntity orderEntity = userOrderMapper.selectByPrimaryKey(2L);
        System.out.println(orderEntity);
    }

    @Test
    void sendSingleMsgToMq() throws InterruptedException {
        ConnectionFactory consumeConnectionFactory = (ConnectionFactory) context.getBean("consumeIncrementConnectionFactory");
        //ConnectionFactory produceConnectionFactory = (ConnectionFactory) context.getBean("produceIncrementConnectionFactory");
        Connection connection = consumeConnectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        String msg = "{\"id\":2,\"tableName\":\"t_order\",\"data\":{\"userId\":\"tom\",\"productCode\":\"99\",\"quantity\":200}}";
        try {
            channel.basicPublish(incrementProperties.getExchange(), incrementProperties.getRoutingKey(), MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            connection.close();
        }
        TimeUnit.MILLISECONDS.sleep(2000);
    }

    @Test
    void sendAllMq() {
        OrderEntity orderEntity = null;
        for (int i = 1; i <= 1000; i++) {
            orderEntity = new OrderEntity(Long.valueOf(Integer.toString(i)), "u_" + i, "c_" + i, i + 1);
            dataSaveHandle.addData(orderEntity);
            allLoadBalance.roundRobin().convertAndSend("route_key_order_all", orderEntity, new CorrelationData(String.valueOf(i)));

            if (i % 200 == 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        dataRecoverHandle.setSend(true);
    }

    @Test
    void recoverSendMqTest() throws InterruptedException {
        int i = 1001;
        OrderEntity orderEntity = new OrderEntity(Long.valueOf(Integer.toString(i)), "u_" + i, "c_" + i, i + 1);
        dataSaveHandle.addData(orderEntity);
        dataRecoverHandle.setSend(true);
        TimeUnit.MILLISECONDS.sleep(2000);
    }
}

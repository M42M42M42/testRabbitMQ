package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {
    private static final String EXCHANGE_NAME = "m42_test_exchange";
    private static final String ROUTING_KEY = "m42_test_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // RabbitMQ服务器地址（本地部署localhost）
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 创建连接、信道
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // 声明交换机（类型为 direct，持久化）
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            // 发送消息
            String message = "hello, RabbitMQ (Java)!";
            channel.basicPublish(
                    EXCHANGE_NAME,      // 交换机名称
                    ROUTING_KEY,        // 路由键
                    null,               // 消息属性（如持久化等）
                    message.getBytes() // 消息体
            );

            System.out.println("已发送消息：" + message);
        }

    }
}

package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
    private static final String EXCHANGE_NAME = "m42_test_exchange";
    private static final String QUEUE_NAME = "m42_test_queue";
    private static final String ROUTING_KEY = "m42_test_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 创建连接、信道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

        // 声明队列（持久化，非排他，非自动删除）
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 绑定队列到交换机（通过路由键）
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        System.out.println("等待接受消息...");

        // 定义消息处理回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息：" + message);

            // 手动处理消息（确保消息被处理之后再删除，避免丢失）
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        // 启动消费者（关闭自动确认，改为手动确认）
        channel.basicConsume(
                QUEUE_NAME,         // 队列名
                false,              // autoAck = false （手动确认）
                deliverCallback,
                consumerTag -> {
                }
        );
    }
}

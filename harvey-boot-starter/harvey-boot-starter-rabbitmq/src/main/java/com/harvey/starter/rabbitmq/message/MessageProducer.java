package com.harvey.starter.rabbitmq.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-12 11:41
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final AmqpTemplate amqpTemplate;

    /**
     * 发送消息到myQueue
     * @param message
     */
    public void sendMessage(String message) {
        amqpTemplate.convertAndSend("myQueue", message);
        log.info("发送普通消息: {}", message);
    }

    /**
     * 发送消息到指定交换机和路由绑定的队列
     * @param message
     */
    public void sendMessageByDirect(String message) {
        amqpTemplate.convertAndSend("myDirectExchange", "rk.002", message);
        log.info("发送消息到Direct Exchange绑定的队列: {}", message);
    }

    public void sendDelayMessage(String text) {
        MessageProperties properties = new MessageProperties();
        properties.setDelayLong(10000L);  // 延迟10秒
        Message message = new Message(text.getBytes(), properties);
        amqpTemplate.send("delayedExchange", "myRoutingKey", message);
        log.info("发送延迟消息: {}", text);
    }
}

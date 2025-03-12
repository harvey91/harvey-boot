package com.harvey.rabbit.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-12 11:41
 **/
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
        System.out.println("发送消息: " + message);
    }
}

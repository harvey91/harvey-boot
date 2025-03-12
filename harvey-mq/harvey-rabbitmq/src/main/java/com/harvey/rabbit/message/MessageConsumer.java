package com.harvey.rabbit.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-12 11:42
 **/
@Service
public class MessageConsumer {

    /**
     * 监听myQueue队列
     * @param message
     */
    @RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        System.out.println("收到消息: " + message);
    }
}

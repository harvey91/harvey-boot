package com.harvey.starter.rabbitmq.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-12 11:42
 **/
@Slf4j
@Service
public class MessageConsumer {

    /**
     * 监听myQueue队列
     * @param message
     */
    @RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        log.info("收到消息: {}", message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue("myQueue"),
            exchange = @Exchange("myDirectExchange"),
            key = {"rk.002"})})
    public void receiveDirectMessage(String message) {
        log.info(">>>>>>>>>>>>>>>>>>> 收到myDirectExchange消息: {}", message);
    }
}

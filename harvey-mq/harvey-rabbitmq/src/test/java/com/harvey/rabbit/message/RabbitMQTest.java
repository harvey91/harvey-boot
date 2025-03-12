package com.harvey.rabbit.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Harvey
 * @date 2025-03-12 13:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {
    @Autowired
    private MessageProducer messageProducer;

    @Test
    public void testSendMessage() {
        messageProducer.sendMessage("Hello, RabbitMQ!");
    }
}

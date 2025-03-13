package com.harvey.rocket.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Harvey
 * @date 2025-03-13 14:19
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketMQTest {

    @Autowired
    private MessageProducer messageProducer;

    @Test
    public void testSendMessage() {
        messageProducer.sendMessage("my-topic", "Hello RocketMQ!");
    }
}

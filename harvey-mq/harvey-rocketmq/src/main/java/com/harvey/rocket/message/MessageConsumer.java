package com.harvey.rocket.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-13 14:16
 **/
@Slf4j
@Service
@RocketMQMessageListener(topic = "my-topic", consumerGroup = "my-consumer-group")
public class MessageConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("收到消息: {}", message);
    }
}

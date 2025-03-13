package com.harvey.rocket.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-13 14:18
 **/
@Slf4j
@Service
@RocketMQMessageListener(
        topic = "my-order-topic",
        consumerGroup = "my-order-consumer-group",
        messageModel = MessageModel.CLUSTERING,  // 集群模式
        consumeMode = ConsumeMode.ORDERLY        // 顺序消费
)
public class OrderMessageConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("收到顺序消息: {}", message);
    }
}
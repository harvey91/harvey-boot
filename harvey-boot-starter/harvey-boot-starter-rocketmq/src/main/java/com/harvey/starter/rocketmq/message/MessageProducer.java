package com.harvey.starter.rocketmq.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Harvey
 * @date 2025-03-13 14:13
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic, String message) {
        rocketMQTemplate.convertAndSend(topic, message);
        log.info("发送消息: {}", message);
    }

    /**
     * 发送延迟消息
     * @param topic
     * @param message
     * @param delayLevel
     */
    public void sendDelayMessage(String topic, String message, int delayLevel) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build(), 3000, delayLevel);
    }

    /**
     * 发送批量消息
     * @param topic
     * @param messages
     */
    public void sendBatchMessages(String topic, List<String> messages) {
        List<Message<String>> messageList = messages.stream()
                .map(msg -> MessageBuilder.withPayload(msg).build())
                .collect(Collectors.toList());
        rocketMQTemplate.syncSend(topic, messageList);
    }
}

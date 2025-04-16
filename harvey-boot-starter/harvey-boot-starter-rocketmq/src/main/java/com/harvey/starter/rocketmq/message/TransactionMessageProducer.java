package com.harvey.starter.rocketmq.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2025-03-13 14:14
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionMessageProducer {
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 发送事务消息
     * @param topic
     * @param message
     */
    public void sendTransactionMessage(String topic, String message) {
        rocketMQTemplate.sendMessageInTransaction(topic, MessageBuilder.withPayload(message).build(), null);
    }
}

@RocketMQTransactionListener
class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // 执行本地事务
        try {
            // 业务逻辑...
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // 检查本地事务状态
        return RocketMQLocalTransactionState.COMMIT;
    }
}

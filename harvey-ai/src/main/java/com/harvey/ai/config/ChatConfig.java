package com.harvey.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harvey
 * @date 2025-03-03 18:03
 **/
@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你是一个智能机器人，你的名字叫小杨。").build();
    }
}

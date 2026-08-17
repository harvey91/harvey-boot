package com.harvey.ai.config;

import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harvey
 * @date 2025-03-03 18:03
 **/
@Configuration
public class ChatConfig {

    /**
     * 基于内存的方式实现对话记忆
     * @return
     */
    @Bean
    InMemoryChatMemory inMemoryChatMemory() {
        return new InMemoryChatMemory();
    }
}
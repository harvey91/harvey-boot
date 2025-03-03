package com.harvey.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harvey
 * @date 2025-03-03 18:00
 **/
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatClient chatClient;

    @PostMapping(value = "/chat")
    public String chat(@RequestBody String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}

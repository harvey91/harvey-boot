package com.harvey.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Harvey
 * @date 2025-03-03 18:00
 **/
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatClient chatClient;

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chat(@RequestBody String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                // 添加事件区分正常回答消息(message)和错误消息(error)
                .content().map(content -> ServerSentEvent.builder(content).event("message").build())
                // 添加问题回答结束标识
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }

}

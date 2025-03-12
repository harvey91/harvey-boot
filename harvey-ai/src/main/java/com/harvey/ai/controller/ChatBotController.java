package com.harvey.ai.controller;

import com.harvey.ai.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author Harvey
 * @date 2025-03-03 18:00
 **/
@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatClient chatClient;
    private final OpenAiChatModel openAiChatModel;
//    private final OpenAiImageModel imageModel;


    /**
     * 普遍对话
     * @param dto
     * @return
     */
    @PostMapping(value = "/chat")
    public Map<String, String> chat(@RequestBody @Validated MessageDto dto) {
        log.info("普通对话内容：“{}”", dto.getMessage());
        String content = chatClient.prompt()
                .user(dto.getMessage())
                .call().content();
        log.info("机器人回答：“{}”", content);
        return Map.of("data", content);
    }

    /**
     * 流式对话
     * @param dto
     * @return
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody @Validated MessageDto dto) {
        log.info("流式对话内容：{}", dto.getMessage());
        return chatClient.prompt()
                .user(dto.getMessage())
                .stream().content();
                // 添加事件区分正常回答消息(message)和错误消息(error)
//                .map(content -> ServerSentEvent.builder(content).event("message").build())
                // 添加问题回答结束标识([DONE])
//                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
//                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }

    /**
     * 文字生成图片
     * @param message
     * @return
     */
//    @PostMapping(value = "genPic")
//    public String genPic(@RequestBody String message) {
//        OpenAiImageOptions options = OpenAiImageOptions.builder()
//                .withQuality("hd")
//                .withN(1)
//                .withHeight(1024)
//                .withWidth(1024).build();
//        ImagePrompt imagePrompt = new ImagePrompt(message, options);
//        ImageResponse response = imageModel.call(imagePrompt);
//        return response.getResult().getOutput().getUrl();
//    }
}

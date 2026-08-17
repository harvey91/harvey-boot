package com.harvey.ai.controller;

import com.harvey.ai.domain.MessageDto;
import com.harvey.ai.service.AiChatService;
import com.harvey.common.result.RespResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author Harvey
 * @date 2025-03-03 18:00
 **/
@Slf4j
@Tag(name = "AI 对话")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatBotController {

    private final AiChatService aiChatService;

    /**
     * 普遍对话(多轮记忆, 可指定模型与会话, 可选知识库检索)
     * @param dto
     * @return
     */
    @Operation(summary = "普通对话(支持知识库)")
    @PostMapping(value = "/chat")
    public RespResult<AiChatService.ChatResult> chat(@RequestBody @Validated MessageDto dto) {
        boolean useKb = Boolean.TRUE.equals(dto.getUseKnowledgeBase());
        log.info("普通对话内容：{}, 模型: {}, 会话: {}, 知识库: {}", dto.getMessage(), dto.getModelId(), dto.getConversationId(), useKb);
        return RespResult.success(aiChatService.chat(dto.getModelId(), dto.getConversationId(), dto.getMessage(),
                dto.getKnowledgeBaseIds(), useKb));
    }

    /**
     * 流式对话
     * @param dto
     * @return
     */
    @Operation(summary = "流式对话(支持知识库)")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody @Validated MessageDto dto) {
        boolean useKb = Boolean.TRUE.equals(dto.getUseKnowledgeBase());
        log.info("流式对话内容：{}, 模型: {}, 会话: {}, 知识库: {}", dto.getMessage(), dto.getModelId(), dto.getConversationId(), useKb);
        return aiChatService.streamChat(dto.getModelId(), dto.getConversationId(), dto.getMessage(),
                dto.getKnowledgeBaseIds(), useKb);
    }
}
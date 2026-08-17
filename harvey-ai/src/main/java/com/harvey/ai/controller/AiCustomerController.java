package com.harvey.ai.controller;

import com.harvey.ai.model.dto.AiWorkOrderDto;
import com.harvey.ai.service.AiCustomerService;
import com.harvey.ai.service.AiWorkOrderService;
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
 * 智能客服(客户侧): 对话 + 转人工留言
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Tag(name = "AI 智能客服")
@RestController
@RequestMapping("/ai/customer")
@RequiredArgsConstructor
public class AiCustomerController {

    private final AiCustomerService customerService;
    private final AiWorkOrderService workOrderService;

    @Operation(summary = "客服对话(FAQ + 知识库)")
    @PostMapping("/chat")
    public RespResult<AiCustomerService.CustomerChatResult> chat(@RequestBody CustomerMessageDto dto) {
        log.info("客服对话内容：{}, 会话: {}, 知识库: {}", dto.getMessage(), dto.getConversationId(), dto.getKnowledgeBaseIds());
        return RespResult.success(customerService.chat(dto.getModelId(), dto.getConversationId(), dto.getMessage(),
                dto.getKnowledgeBaseIds()));
    }

    @Operation(summary = "客服流式对话")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody CustomerMessageDto dto) {
        return customerService.streamChat(dto.getModelId(), dto.getConversationId(), dto.getMessage(),
                dto.getKnowledgeBaseIds());
    }

    @Operation(summary = "转人工/留言, 生成工单")
    @PostMapping("/order")
    public RespResult<Long> createOrder(@RequestBody @Validated AiWorkOrderDto dto) {
        Long id = workOrderService.createOrder(dto.getConversationId(), dto.getContact(), dto.getContent());
        return RespResult.success(id);
    }

    /**
     * 客服对话请求体
     */
    @lombok.Data
    public static class CustomerMessageDto {
        @jakarta.validation.constraints.NotBlank(message = "不能发送空白消息")
        private String message;
        private Long modelId;
        private String conversationId;
        private java.util.List<Long> knowledgeBaseIds;
    }
}
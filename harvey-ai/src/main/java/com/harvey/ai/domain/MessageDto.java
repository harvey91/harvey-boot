package com.harvey.ai.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2025-03-13 18:34
 **/
@Data
public class MessageDto {

    @NotBlank(message = "不能发送空白消息")
    private String message;

    /**
     * 模型配置ID, 为空使用默认模型
     */
    private Long modelId;

    /**
     * 会话ID, 为空自动生成; 相同会话ID保留多轮上下文
     */
    private String conversationId;

    /**
     * 是否启用知识库检索(默认 false)
     */
    private Boolean useKnowledgeBase;

    /**
     * 指定检索的知识库ID, 为空则检索全部启用的知识库
     */
    private List<Long> knowledgeBaseIds;
}
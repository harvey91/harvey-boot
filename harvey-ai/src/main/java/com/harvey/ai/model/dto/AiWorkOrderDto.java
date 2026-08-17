package com.harvey.ai.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
public class AiWorkOrderDto {

    private Long id;

    /** 会话ID */
    private String conversationId;

    /** 联系方式 */
    private String contact;

    private String content;

    /** 人工回复 */
    private String reply;
}
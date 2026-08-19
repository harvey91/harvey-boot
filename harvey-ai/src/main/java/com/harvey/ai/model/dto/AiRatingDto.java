package com.harvey.ai.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 客服评价请求
 *
 * @author harvey
 * @since 2026-08-18
 */
@Data
public class AiRatingDto {

    @NotBlank(message = "会话ID不能为空")
    private String conversationId;

    @Min(value = 1, message = "评价取值错误")
    @Max(value = 2, message = "评价取值错误")
    private Integer rating;
}
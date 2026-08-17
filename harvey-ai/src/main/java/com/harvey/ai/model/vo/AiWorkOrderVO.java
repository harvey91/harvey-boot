package com.harvey.ai.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiWorkOrderVO")
public class AiWorkOrderVO {
    private Long id;

    @Schema(title = "conversationId", description = "会话ID")
    private String conversationId;

    @Schema(title = "contact", description = "联系方式")
    private String contact;

    @Schema(title = "content", description = "客户问题")
    private String content;

    @Schema(title = "status", description = "状态(0待处理,1已回复,2已关闭)")
    private Integer status;

    @Schema(title = "reply", description = "人工回复")
    private String reply;

    @Schema(title = "replyTime", description = "回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;

    @Schema(title = "createTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
package com.harvey.ai.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author harvey
 * @since 2026-08-18
 */
@Data
@Schema(title = "AiConversationVO")
public class AiConversationVO {
    private Long id;

    @Schema(title = "conversationId", description = "会话ID")
    private String conversationId;

    @Schema(title = "title", description = "会话标题(首问摘要)")
    private String title;

    @Schema(title = "messageCount", description = "消息数")
    private Integer messageCount;

    @Schema(title = "lastMessageTime", description = "最后消息时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMessageTime;

    @Schema(title = "createTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
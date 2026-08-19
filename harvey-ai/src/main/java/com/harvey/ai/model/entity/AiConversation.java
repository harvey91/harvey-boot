package com.harvey.ai.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * AI客服会话记录表
 * </p>
 *
 * @author harvey
 * @since 2026-08-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_conversation")
@Schema(title = "AiConversation对象", description = "AI客服会话记录表")
public class AiConversation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "conversationId", description = "会话ID")
    private String conversationId;

    @Schema(title = "title", description = "会话标题(首问摘要)")
    private String title;

    @Schema(title = "status", description = "状态(0进行中,1已结束)")
    private Integer status;

    @Schema(title = "messageCount", description = "消息数")
    private Integer messageCount;

    @Schema(title = "lastMessageTime", description = "最后消息时间")
    private LocalDateTime lastMessageTime;

}
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
 * AI客服消息记录表
 * </p>
 *
 * @author harvey
 * @since 2026-08-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_chat_message")
@Schema(title = "AiChatMessage对象", description = "AI客服消息记录表")
public class AiChatMessage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "conversationId", description = "会话ID")
    private String conversationId;

    @Schema(title = "role", description = "角色(USER/BOT)")
    private String role;

    @Schema(title = "content", description = "消息内容")
    private String content;

    @Schema(title = "citations", description = "引用来源(JSON)")
    private String citations;

    @Schema(title = "needHuman", description = "是否需转人工(0否,1是)")
    private Integer needHuman;

    @Schema(title = "rating", description = "评价(0未评价,1满意,2不满意)")
    private Integer rating;

    @Schema(title = "ratingTime", description = "评价时间")
    private LocalDateTime ratingTime;

}
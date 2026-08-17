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
 * AI客服工单表
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_work_order")
@Schema(title = "AiWorkOrder对象", description = "AI客服工单表")
public class AiWorkOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private LocalDateTime replyTime;

}
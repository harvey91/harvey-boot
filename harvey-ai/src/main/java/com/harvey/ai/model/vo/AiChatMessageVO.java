package com.harvey.ai.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harvey.ai.service.AiCustomerService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author harvey
 * @since 2026-08-18
 */
@Data
@Schema(title = "AiChatMessageVO")
public class AiChatMessageVO {
    private Long id;

    @Schema(title = "conversationId", description = "会话ID")
    private String conversationId;

    @Schema(title = "role", description = "角色(USER/BOT)")
    private String role;

    @Schema(title = "content", description = "消息内容")
    private String content;

    @Schema(title = "citations", description = "引用来源")
    private List<AiCustomerService.CustomerCitation> citations;

    @Schema(title = "needHuman", description = "是否需转人工")
    private Integer needHuman;

    @Schema(title = "rating", description = "评价(0未评价,1满意,2不满意)")
    private Integer rating;

    @Schema(title = "ratingTime", description = "评价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ratingTime;

    @Schema(title = "createTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
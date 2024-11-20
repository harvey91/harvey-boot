package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
* 系统通知表 VO类
*
* @author harvey
* @since 2024-11-20
*/
@Data
public class NoticeVO {

    @Schema(description = "通知ID")
    private Long id;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "通知类型")
    private Integer type;

    @Schema(description = "通知状态(0未发布，1已发布，2已撤回)")
    private Integer status;

    @Schema(description = "通知等级")
    private String level;

    @Schema(description = "通知目标类型(1全体，2指定人)")
    private Integer targetType;

    @Schema(description = "发布人")
    private Long publisherName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "撤回时间")
    private LocalDateTime revokeTime;
}

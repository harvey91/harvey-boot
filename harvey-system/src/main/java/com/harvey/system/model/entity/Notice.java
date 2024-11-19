package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 系统通知表
 * </p>
 *
 * @author harvey
 * @since 2024-11-19
 */
@Data
@TableName("sys_notice")
@Schema(name = "Notice", description = "系统通知表")
public class Notice extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;


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

    @Schema(description = "发布人id")
    private Long publisherId;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "撤回时间")
    private LocalDateTime revokeTime;
}

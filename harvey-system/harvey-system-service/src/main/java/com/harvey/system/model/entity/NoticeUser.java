package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 系统通知指定用户表
 * </p>
 *
 * @author harvey
 * @since 2024-11-20
 */
@Data
@Builder
@TableName("sys_notice_user")
@Schema(name = "NoticeUser", description = "系统通知指定用户表")
public class NoticeUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id",description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "通知id")
    private Long noticeId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "是否已读")
    private Long isRead;

    @Schema(title = "createTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    @Schema(title = "readTime", description = "已读时间")
    public LocalDateTime readTime;
}

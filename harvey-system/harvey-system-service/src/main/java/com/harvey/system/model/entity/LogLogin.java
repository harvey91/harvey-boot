package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;

import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 登陆日志表
 * </p>
 *
 * @author harvey
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("sys_log_login")
@Schema(name = "LogLogin", description = "登陆日志表")
public class LogLogin extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "地点")
    private String location;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "结果")
    private Integer result;
}

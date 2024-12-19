package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author harvey
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_log_op")
@Schema(name = "LogOp", description = "操作日志表")
public class LogOp extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "操作用户id")
    private Long userId;

    @Schema(description = "操作员")
    private String operator;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "请求路径")
    private String requestUri;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求参数")
    private String params;

    @Schema(description = "详情")
    private String detail;

    @Schema(description = "执行时长(ms)")
    private Long duration;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "地点")
    private String location;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "结果(1成功，2异常)")
    private Integer result;
}

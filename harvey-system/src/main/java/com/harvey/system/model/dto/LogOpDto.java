package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
* 操作日志表 DTO类
*
* @author harvey
* @since 2024-11-21
*/
@Data
@Builder
public class LogOpDto {

    private Long id;

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

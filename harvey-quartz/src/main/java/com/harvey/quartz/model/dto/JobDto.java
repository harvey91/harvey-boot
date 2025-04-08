package com.harvey.quartz.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 定时任务调度 DTO类
*
* @author harvey
* @since 2025-04-08
*/
@Data
public class JobDto {

    private Long id;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务组名")
    private String jobGroup;

    @Schema(description = "调用目标字符串")
    private String invokeTarget;

    @Schema(description = "cron执行表达式")
    private String cronExpression;

    @Schema(description = "计划执行错误策略（1立即执行 2执行一次 3放弃执行）")
    private Integer misfirePolicy;

    @Schema(description = "是否并发执行（0允许 1禁止）")
    private Integer concurrent;

    @Schema(title = "enabled", description = "是否启用：0禁用，1启用")
    private Integer enabled;
}

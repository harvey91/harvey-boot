package com.harvey.quartz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;

import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 定时任务调度
 * </p>
 *
 * @author harvey
 * @since 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("sys_job")
@Schema(name = "Job", description = "定时任务调度")
public class Job extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

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
}

package com.harvey.quartz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;

import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 定时任务调度日志
 * </p>
 *
 * @author harvey
 * @since 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("sys_job_log")
@Schema(name = "JobLog", description = "定时任务调度日志")
public class JobLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务组名")
    private String jobGroup;

    @Schema(description = "调用目标字符串")
    private String invokeTarget;

    @Schema(description = "日志信息")
    private String jobMessage;

    @Schema(description = "异常信息")
    private String exceptionInfo;
}

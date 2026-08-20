package com.harvey.starter.quartz.schedule;

/**
 * 定时任务常量
 *
 * @author Harvey
 * @since 2026-08-06
 */
public class ScheduleConstants {

    /**
     * JobDataMap 中存放 Job 实体的 key
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 任务前缀
     */
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /**
     * 是否禁用任务
     */
    public static final int JOB_DISABLED = 0;

    /**
     * 是否启用任务
     */
    public static final int JOB_ENABLED = 1;

    /**
     * 允许并发执行
     */
    public static final int CONCURRENT_YES = 0;

    /**
     * 禁止并发执行
     */
    public static final int CONCURRENT_NO = 1;

    /**
     * 计划执行错误策略：默认(放弃执行)
     */
    public static final int MISFIRE_DEFAULT = 0;

    /**
     * 计划执行错误策略：立即执行(补偿执行所有错过的任务)
     */
    public static final int MISFIRE_IGNORE_MISFIRES = 1;

    /**
     * 计划执行错误策略：执行一次
     */
    public static final int MISFIRE_FIRE_AND_PROCEED = 2;

    /**
     * 计划执行错误策略：放弃执行
     */
    public static final int MISFIRE_DO_NOTHING = 3;

    private ScheduleConstants() {
    }
}

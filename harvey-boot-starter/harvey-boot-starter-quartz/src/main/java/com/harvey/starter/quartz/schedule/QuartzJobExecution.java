package com.harvey.starter.quartz.schedule;

import com.harvey.starter.quartz.model.entity.Job;

/**
 * 允许并发执行的定时任务
 *
 * @author Harvey
 * @since 2026-08-06
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(org.quartz.JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}

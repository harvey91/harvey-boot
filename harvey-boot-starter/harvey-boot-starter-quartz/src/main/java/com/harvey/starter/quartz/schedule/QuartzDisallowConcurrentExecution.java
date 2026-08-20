package com.harvey.starter.quartz.schedule;

import com.harvey.starter.quartz.model.entity.Job;
import org.quartz.DisallowConcurrentExecution;

/**
 * 禁止并发执行的定时任务
 *
 * @author Harvey
 * @since 2026-08-06
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(org.quartz.JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}

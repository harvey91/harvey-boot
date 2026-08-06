package com.harvey.quartz.schedule;

import com.harvey.common.utils.StringUtils;
import com.harvey.quartz.model.entity.Job;
import com.harvey.quartz.model.entity.JobLog;
import com.harvey.quartz.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * 定时任务基类
 *
 * @author Harvey
 * @since 2026-08-06
 */
@Slf4j
public abstract class AbstractQuartzJob extends QuartzJobBean {

    @Autowired
    private JobLogService jobLogService;

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext context) {
        Job job = (Job) context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES);
        JobLog jobLog = new JobLog();
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setInvokeTarget(job.getInvokeTarget());
        jobLog.setCreateTime(LocalDateTime.now());

        long startTime = System.currentTimeMillis();
        try {
            doExecute(context, job);
            jobLog.setJobMessage("任务执行成功，耗时 " + (System.currentTimeMillis() - startTime) + " 毫秒");
        } catch (Exception e) {
            log.error("任务执行失败, jobName: {}, invokeTarget: {}", job.getJobName(), job.getInvokeTarget(), e);
            jobLog.setJobMessage("任务执行失败，耗时 " + (System.currentTimeMillis() - startTime) + " 毫秒");
            jobLog.setExceptionInfo(StringUtils.substring(ExceptionUtils.getStackTrace(e), 0, 2000));
        } finally {
            try {
                jobLogService.createJobLog(jobLog);
            } catch (Exception e) {
                log.error("保存任务执行日志失败", e);
            }
        }
    }

    protected abstract void doExecute(org.quartz.JobExecutionContext context, Job job) throws Exception;
}

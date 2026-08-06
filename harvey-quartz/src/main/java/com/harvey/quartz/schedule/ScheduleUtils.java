package com.harvey.quartz.schedule;

import com.harvey.quartz.model.entity.Job;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * 定时任务调度工具类
 *
 * @author Harvey
 * @since 2026-08-06
 */
public class ScheduleUtils {

    private ScheduleUtils() {
    }

    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, Job job) throws SchedulerException {
        Class<? extends org.quartz.Job> jobClass = getQuartzJobClass(job.getConcurrent());

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(getJobKey(job.getId(), job.getJobGroup()))
                .setJobData(jobDataMap)
                .build();

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job.getMisfirePolicy(), cronScheduleBuilder);

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(job.getId(), job.getJobGroup()))
                .withSchedule(cronScheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        if (job.getEnabled() == null || job.getEnabled() == ScheduleConstants.JOB_DISABLED) {
            scheduler.pauseJob(getJobKey(job.getId(), job.getJobGroup()));
        }
    }

    /**
     * 更新定时任务(先删除旧的再重新创建)
     */
    public static void updateScheduleJob(Scheduler scheduler, Job job) throws SchedulerException {
        JobKey jobKey = getJobKey(job.getId(), job.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        createScheduleJob(scheduler, job);
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId, String jobGroup) throws SchedulerException {
        scheduler.deleteJob(getJobKey(jobId, jobGroup));
    }

    /**
     * 暂停定时任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(getJobKey(jobId, jobGroup));
    }

    /**
     * 恢复定时任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId, String jobGroup) throws SchedulerException {
        scheduler.resumeJob(getJobKey(jobId, jobGroup));
    }

    /**
     * 立即执行一次
     */
    public static void runJob(Scheduler scheduler, Job job) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
        scheduler.triggerJob(getJobKey(job.getId(), job.getJobGroup()), dataMap);
    }

    /**
     * 根据是否允许并发选择任务执行类
     */
    private static Class<? extends org.quartz.Job> getQuartzJobClass(Integer concurrent) {
        if (concurrent != null && concurrent == ScheduleConstants.CONCURRENT_NO) {
            return QuartzDisallowConcurrentExecution.class;
        }
        return QuartzJobExecution.class;
    }

    private static CronScheduleBuilder handleCronScheduleMisfirePolicy(Integer misfirePolicy, CronScheduleBuilder cb) {
        Integer policy = misfirePolicy == null ? ScheduleConstants.MISFIRE_DEFAULT : misfirePolicy;
        return switch (policy) {
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES -> cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED -> cb.withMisfireHandlingInstructionFireAndProceed();
            default -> cb.withMisfireHandlingInstructionDoNothing();
        };
    }
}

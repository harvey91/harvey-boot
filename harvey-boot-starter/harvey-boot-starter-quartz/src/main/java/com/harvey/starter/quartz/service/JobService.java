package com.harvey.starter.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.starter.quartz.mapper.JobMapper;
import com.harvey.starter.quartz.mapstruct.JobConverter;
import com.harvey.starter.quartz.model.dto.JobDto;
import com.harvey.starter.quartz.model.entity.Job;
import com.harvey.starter.quartz.model.query.JobQuery;
import com.harvey.starter.quartz.schedule.ScheduleUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 定时任务调度 服务实现类
 *
 * @author harvey
 * @since 2025-04-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobService extends ServiceImpl<JobMapper, Job> {
    private final JobMapper mapper;
    private final JobConverter converter;
    private final Scheduler scheduler;

    /**
     * 应用启动时,将数据库中的启用任务注册到调度器
     */
    @PostConstruct
    public void initJobs() {
        List<Job> jobs = this.list();
        for (Job job : jobs) {
            if (job.getEnabled() == null || job.getEnabled() == 0) {
                continue;
            }
            try {
                ScheduleUtils.createScheduleJob(scheduler, job);
                log.info("初始化定时任务成功, jobName: {}, cron: {}", job.getJobName(), job.getCronExpression());
            } catch (SchedulerException e) {
                log.error("初始化定时任务失败, jobName: {}", job.getJobName(), e);
            }
        }
    }

    public Page<Job> queryPage(JobQuery query) {
        Page<Job> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<Job>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Job::getJobName, query.getKeywords())
                .orderByDesc(Job::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveJob(JobDto dto) {
        Job entity = converter.toEntity(dto);
        this.save(entity);
        try {
            ScheduleUtils.createScheduleJob(scheduler, entity);
        } catch (SchedulerException e) {
            log.error("创建定时任务失败, jobName: {}", entity.getJobName(), e);
            throw new BusinessException("创建定时任务失败,请检查 cron 表达式");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateJob(JobDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Job entity = converter.toEntity(dto);
        this.updateById(entity);
        try {
            ScheduleUtils.updateScheduleJob(scheduler, entity);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败, jobId: {}", entity.getId(), e);
            throw new BusinessException("更新定时任务失败,请检查 cron 表达式");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        for (Long id : ids) {
            Job job = this.getById(id);
            if (job == null) {
                continue;
            }
            try {
                ScheduleUtils.deleteScheduleJob(scheduler, job.getId(), job.getJobGroup());
            } catch (SchedulerException e) {
                log.error("删除定时任务失败, jobId: {}", id, e);
            }
        }
        this.removeByIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateJobStatus(JobDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Job entity = this.getById(dto.getId());
        if (entity == null) {
            throw new BadParameterException();
        }
        entity.setEnabled(dto.getEnabled());
        this.updateById(entity);
        try {
            ScheduleUtils.updateScheduleJob(scheduler, entity);
        } catch (SchedulerException e) {
            log.error("更新定时任务状态失败, jobId: {}", entity.getId(), e);
            throw new BusinessException("更新定时任务状态失败");
        }
    }

    public void runJob(JobDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Job entity = this.getById(dto.getId());
        if (entity == null) {
            throw new BadParameterException();
        }
        try {
            ScheduleUtils.runJob(scheduler, entity);
        } catch (SchedulerException e) {
            log.error("立即执行定时任务失败, jobId: {}", entity.getId(), e);
            throw new BusinessException("立即执行定时任务失败");
        }
    }
}

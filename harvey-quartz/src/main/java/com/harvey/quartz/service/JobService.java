package com.harvey.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.quartz.model.query.JobQuery;
import com.harvey.quartz.mapstruct.JobConverter;
import com.harvey.quartz.model.dto.JobDto;
import com.harvey.quartz.model.entity.Job;
import com.harvey.quartz.mapper.JobMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.List;

/**
 * 定时任务调度 服务实现类
 *
 * @author harvey
 * @since 2025-04-08
 */
@Service
@RequiredArgsConstructor
public class JobService extends ServiceImpl<JobMapper, Job> {
    private final JobMapper mapper;
    private final JobConverter converter;

    public Page<Job> queryPage(JobQuery query) {
        Page<Job> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<Job>()
//                .like(StringUtils.isNotBlank(query.getKeywords()), Job::getName, query.getKeywords())
                .orderByAsc(Job::getSort);
        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveJob(JobDto dto) {
        Job entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateJob(JobDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Job entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        this.removeByIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateJobStatus(JobDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Job entity = getById(dto.getId());
        entity.setEnabled(dto.getEnabled());
        this.updateById(entity);
    }
}

package com.harvey.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.quartz.model.query.JobLogQuery;
import com.harvey.quartz.mapstruct.JobLogConverter;
import com.harvey.quartz.model.dto.JobLogDto;
import com.harvey.quartz.model.entity.JobLog;
import com.harvey.quartz.mapper.JobLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.List;

/**
 * 定时任务调度日志 服务实现类
 *
 * @author harvey
 * @since 2025-04-08
 */
@Service
@RequiredArgsConstructor
public class JobLogService extends ServiceImpl<JobLogMapper, JobLog> {
    private final JobLogMapper mapper;
    private final JobLogConverter converter;

    public Page<JobLog> queryPage(JobLogQuery query) {
        Page<JobLog> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<JobLog> queryWrapper = new LambdaQueryWrapper<JobLog>()
//                .like(StringUtils.isNotBlank(query.getKeywords()), JobLog::getName, query.getKeywords())
                .orderByAsc(JobLog::getSort);
        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveJobLog(JobLogDto dto) {
        JobLog entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateJobLog(JobLogDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        JobLog entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        this.removeByIds(ids);
    }
}

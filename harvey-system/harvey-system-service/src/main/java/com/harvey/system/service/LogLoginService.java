package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.query.LogLoginQuery;
import com.harvey.system.mapstruct.LogLoginConverter;
import com.harvey.system.model.dto.LogLoginDto;
import com.harvey.system.model.entity.LogLogin;
import com.harvey.system.mapper.LogLoginMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.utils.StringUtils;
import com.harvey.common.exception.BadParameterException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 登陆日志表 服务实现类
 *
 * @author harvey
 * @since 2024-11-21
 */
@Service
@RequiredArgsConstructor
public class LogLoginService extends ServiceImpl<LogLoginMapper, LogLogin> {
    private final LogLoginMapper mapper;
    private final LogLoginConverter converter;

    public Page<LogLogin> queryPage(LogLoginQuery query) {
        Page<LogLogin> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<LogLogin> queryWrapper = new LambdaQueryWrapper<LogLogin>()
                .like(StringUtils.isNotBlank(query.getKeywords()), LogLogin::getUsername, query.getKeywords())
                .orderByDesc(LogLogin::getId);
        return page(page, queryWrapper);
    }

    @Async("asyncExecutor")
    @Transactional(rollbackFor = Throwable.class)
    public void saveLogLogin(LogLoginDto dto) {
        LogLogin entity = converter.toEntity(dto);
        save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateLogLogin(LogLoginDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        LogLogin entity = converter.toEntity(dto);
        updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }
}

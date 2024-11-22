package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.query.LogOpQuery;
import com.harvey.system.mapstruct.LogOpConverter;
import com.harvey.system.model.dto.LogOpDto;
import com.harvey.system.model.entity.LogOp;
import com.harvey.system.mapper.LogOpMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.utils.StringUtils;
import com.harvey.system.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.List;

/**
 * 操作日志表 服务实现类
 *
 * @author harvey
 * @since 2024-11-21
 */
@Service
@RequiredArgsConstructor
public class LogOpService extends ServiceImpl<LogOpMapper, LogOp> {
    private final LogOpMapper mapper;
    private final LogOpConverter converter;

    public Page<LogOp> queryPage(LogOpQuery query) {
        Page<LogOp> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<LogOp> queryWrapper = new LambdaQueryWrapper<LogOp>()
                .eq(LogOp::getResult, query.getResult())
                .like(StringUtils.isNotBlank(query.getKeywords()), LogOp::getModule, query.getKeywords())
                .orderByDesc(LogOp::getId);
        return page(page, queryWrapper);
    }


    @Transactional(rollbackFor = Throwable.class)
    public void saveLogOp(LogOpDto dto) {
        LogOp entity = converter.toEntity(dto);
        save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateLogOp(LogOpDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        LogOp entity = converter.toEntity(dto);
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

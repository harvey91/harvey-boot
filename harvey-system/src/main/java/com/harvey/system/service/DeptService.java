package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.DeptMapper;
import com.harvey.system.mapstruct.DeptConverter;
import com.harvey.system.model.dto.DeptDto;
import com.harvey.system.model.entity.Dept;
import com.harvey.system.model.query.DeptQuery;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-29
 */
@Service
@RequiredArgsConstructor
public class DeptService extends ServiceImpl<DeptMapper, Dept> {
    private final DeptMapper mapper;
    private final DeptConverter converter;

    public Page<Dept> queryPage(DeptQuery query) {
        Page<Dept> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<Dept>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Dept::getDeptName, query.getKeywords())
                .orderByAsc(Dept::getSort);
        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveDept(DeptDto dto) {
        Dept entity = converter.toEntity(dto);
        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateDept(DeptDto dto) {
        Dept entity = converter.toEntity(dto);
        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }
}

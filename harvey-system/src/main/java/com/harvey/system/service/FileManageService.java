package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import com.harvey.system.mapper.FileManageMapper;
import com.harvey.system.mapstruct.FileManageConverter;
import com.harvey.system.model.dto.FileManageDto;
import com.harvey.system.model.entity.FileManage;
import com.harvey.system.model.query.FileManageQuery;
import com.harvey.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 文件管理 服务实现类
 *
 * @author harvey
 * @since 2024-12-05
 */
@Service
@RequiredArgsConstructor
public class FileManageService extends ServiceImpl<FileManageMapper, FileManage> {
    private final FileManageMapper mapper;
    private final FileManageConverter converter;

    public Page<FileManage> queryPage(FileManageQuery query) {
        Page<FileManage> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<FileManage> queryWrapper = new LambdaQueryWrapper<FileManage>()
                .like(StringUtils.isNotBlank(query.getKeywords()), FileManage::getName, query.getKeywords())
                .orderByDesc(FileManage::getId);
        return page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveFileManage(FileManageDto dto) {
        FileManage entity = converter.toEntity(dto);
        save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateFileManage(FileManageDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        FileManage entity = converter.toEntity(dto);
        updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        removeById(id);
    }
}

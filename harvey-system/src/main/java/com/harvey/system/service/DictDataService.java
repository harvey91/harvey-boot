package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.DictDataMapper;
import com.harvey.system.mapstruct.DictDataConverter;
import com.harvey.system.model.dto.DictDataDto;
import com.harvey.system.model.entity.DictData;
import com.harvey.system.model.query.DictDataQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 字典数据 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Service
@RequiredArgsConstructor
public class DictDataService extends ServiceImpl<DictDataMapper, DictData> {
    private final DictDataMapper mapper;
    private final DictDataConverter converter;

    public Page<DictData> queryPage(DictDataQuery query) {
        String dictCode = query.getDictCode();
        Page<DictData> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>()
                .eq(StringUtils.hasLength(dictCode), DictData::getDictCode, dictCode)
                .orderByAsc(DictData::getSort);
       return page(page, queryWrapper);
    }


    @Transactional(rollbackFor = Throwable.class)
    public void saveDictData(DictDataDto dto) {
        DictData entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateDictData(DictDataDto dto) {
        DictData entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }

}

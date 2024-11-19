package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.DictDataMapper;
import com.harvey.system.model.entity.DictData;
import com.harvey.system.model.query.DictQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 系统字典数据表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Service
public class DictDataService extends ServiceImpl<DictDataMapper, DictData> {

    public Page<DictData> queryPage(DictQuery query) {
        String dictCode = query.getDictCode();
        Page<DictData> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>()
                .eq(StringUtils.hasLength(dictCode), DictData::getDictCode, dictCode)
                .orderByAsc(DictData::getSort);
       return page(page, queryWrapper);
    }
}

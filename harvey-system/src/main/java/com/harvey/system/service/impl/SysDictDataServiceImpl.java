package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.domain.query.DictQueryParam;
import com.harvey.system.entity.SysDictData;
import com.harvey.system.mapper.SysDictDataMapper;
import com.harvey.system.service.ISysDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    @Override
    public Page<SysDictData> queryPage(DictQueryParam queryParam) {
        String dictCode = queryParam.getDictCode();
        Page<SysDictData> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<SysDictData>()
                .eq(StringUtils.hasLength(dictCode), SysDictData::getDictCode, dictCode)
                .orderByAsc(SysDictData::getSort);
       return page(page, queryWrapper);
    }
}

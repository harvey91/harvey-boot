package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.constant.CacheConstant;
import com.harvey.system.mapper.DictMapper;
import com.harvey.system.mapstruct.DictConverter;
import com.harvey.system.model.dto.DictDto;
import com.harvey.system.model.entity.Dict;
import com.harvey.system.model.entity.DictData;
import com.harvey.system.model.query.DictQuery;
import com.harvey.system.model.vo.DictVO;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统字典表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Service
@RequiredArgsConstructor
public class DictService extends ServiceImpl<DictMapper, Dict> {
    private final DictDataService dictDataService;
    private final DictConverter converter;

    public Page<Dict> queryPage(DictQuery query) {
        Page<Dict> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<Dict>()
                .eq(StringUtils.isNotBlank(query.getDictCode()), Dict::getDictCode, query.getDictCode())
                .like(StringUtils.isNotBlank(query.getKeywords()), Dict::getDictName, query.getKeywords())
                .orderByAsc(Dict::getSort);
        return page(page, queryWrapper);
    }

    @CacheEvict(value = CacheConstant.DICT_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void saveDict(DictDto dto) {
        Dict entity = converter.toEntity(dto);
        this.save(entity);
    }

    @CacheEvict(value = CacheConstant.DICT_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void updateDict(DictDto dto) {
        Dict entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @CacheEvict(value = CacheConstant.DICT_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }

    @Cacheable(value = CacheConstant.DICT_KEY, key = "'all'")
    public List<DictVO> queryDictVOList() {
        List<Dict> dictList = list();
        if (CollectionUtils.isEmpty(dictList)) {
            return Collections.emptyList();
        }
        List<DictVO> dictVOList = new ArrayList<>();
        List<String> dictCodeList = dictList.stream().map(Dict::getDictCode).toList();
        List<DictData> dictDataList = dictDataService.list(new LambdaQueryWrapper<DictData>()
                .in(DictData::getDictCode, dictCodeList));
        Map<String, List<DictData>> dataMap = null;
        if (!CollectionUtils.isEmpty(dictDataList)) {
            dataMap = dictDataList.stream().collect(Collectors.groupingBy(DictData::getDictCode));
        }
        for (Dict dict : dictList) {
            DictVO dictVO = new DictVO();
            dictVO.setName(dict.getDictName());
            dictVO.setDictCode(dict.getDictCode());
            if (!CollectionUtils.isEmpty(dataMap)) {
                List<DictData> dataList = dataMap.get(dict.getDictCode());
                List<DictVO.DataVO> dataVOList = new ArrayList<>();
                for (DictData dictData : dataList) {
                    DictVO.DataVO dataVO = new DictVO.DataVO();
                    dataVO.setValue(dictData.getValue());
                    dataVO.setLabel(dictData.getLabel());
                    dataVO.setTag(dictData.getTag());
                    dataVOList.add(dataVO);
                }
                dictVO.setDictDataList(dataVOList);
            }
            dictVOList.add(dictVO);
        }
        return dictVOList;
    }
}

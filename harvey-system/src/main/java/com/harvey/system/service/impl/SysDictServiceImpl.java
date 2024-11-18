package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.domain.query.DictQueryParam;
import com.harvey.system.domain.vo.DictVO;
import com.harvey.system.entity.SysDict;
import com.harvey.system.entity.SysDictData;
import com.harvey.system.mapper.SysDictMapper;
import com.harvey.system.service.ISysDictDataService;
import com.harvey.system.service.ISysDictService;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
    private final ISysDictDataService sysDictDataService;

    @Override
    public Page<SysDict> queryPage(DictQueryParam queryParam) {
        Page<SysDict> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<SysDict>()
                .eq(StringUtils.isNotBlank(queryParam.getDictCode()), SysDict::getDictCode, queryParam.getDictCode())
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), SysDict::getDictName, queryParam.getKeywords())
                .orderByAsc(SysDict::getSort);
        return page(page, queryWrapper);
    }

    @Override
    public List<DictVO> queryDictVOList() {
        List<SysDict> dictList = list();
        if (CollectionUtils.isEmpty(dictList)) {
            return Collections.emptyList();
        }
        List<DictVO> dictVOList = new ArrayList<>();
        List<String> dictCodeList = dictList.stream().map(SysDict::getDictCode).toList();
        List<SysDictData> dictDataList = sysDictDataService.list(new LambdaQueryWrapper<SysDictData>()
                .in(SysDictData::getDictCode, dictCodeList));
        Map<String, List<SysDictData>> dataMap = null;
        if (!CollectionUtils.isEmpty(dictDataList)) {
            dataMap = dictDataList.stream().collect(Collectors.groupingBy(SysDictData::getDictCode));
        }
        for (SysDict dict : dictList) {
            DictVO dictVO = new DictVO();
            dictVO.setName(dict.getDictName());
            dictVO.setDictCode(dict.getDictCode());
            if (!CollectionUtils.isEmpty(dataMap)) {
                List<SysDictData> dataList = dataMap.get(dict.getDictCode());
                List<DictVO.DataVO> dataVOList = new ArrayList<>();
                for (SysDictData dictData : dataList) {
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

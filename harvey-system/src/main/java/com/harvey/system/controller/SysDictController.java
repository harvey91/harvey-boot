package com.harvey.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.domain.vo.DictVO;
import com.harvey.system.entity.SysDict;
import com.harvey.system.entity.SysDictData;
import com.harvey.system.entity.SysRole;
import com.harvey.system.service.ISysDictDataService;
import com.harvey.system.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {
    private final ISysDictService sysDictService;
    private final ISysDictDataService sysDictDataService;

    @GetMapping("/form/{dictId}")
    public RespResult<SysDict> form(@PathVariable(value = "dictId") Long dictId) {
        SysDict sysDict = sysDictService.getById(dictId);
        return RespResult.success(sysDict);
    }

    @GetMapping("/list")
    public RespResult<List<DictVO>> list() {
        List<SysDict> dictList = sysDictService.list();
        if (CollectionUtils.isEmpty(dictList)) {
            return RespResult.success();
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
        return RespResult.success(dictVOList);
    }

    @GetMapping("/page")
    public RespResult<PageResult<SysDict>> page(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        Page<SysDict> page = new Page<>(pageNum, pageSize);
        Page<SysDict> dictPage = sysDictService.page(page);
        return RespResult.success(PageResult.of(dictPage));
    }

    @PostMapping("/add")
    public RespResult<String> add(@RequestBody SysDict sysDict) {
        sysDictService.save(sysDict);
        return RespResult.success();
    }

    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysDict sysDict) {
        sysDictService.updateById(sysDict);
        return RespResult.success();
    }

    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody Long[] ids) {
        sysDictService.removeByIds(Arrays.asList(ids));
        return RespResult.success();
    }
}

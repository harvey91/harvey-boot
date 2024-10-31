package com.harvey.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.domain.query.DictQuery;
import com.harvey.system.entity.SysDictData;
import com.harvey.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 系统字典数据表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/system/dictData")
@RequiredArgsConstructor
public class SysDictDataController {
    private final ISysDictDataService sysDictDataService;

    @GetMapping("/form/{id}")
    public RespResult<SysDictData> form(@PathVariable(value = "id") Long id) {
        SysDictData dictData = sysDictDataService.getById(id);
        return RespResult.success(dictData);
    }

    @GetMapping("/page")
    public RespResult<PageResult<SysDictData>> page(DictQuery query) {
        String dictCode = query.getDictCode();
        Page<SysDictData> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<SysDictData>()
                .eq(StringUtils.hasLength(dictCode), SysDictData::getDictCode, dictCode);
        Page<SysDictData> dictDataPage = sysDictDataService.page(page, queryWrapper);
        return RespResult.success(PageResult.of(dictDataPage));
    }

    @PostMapping("/add")
    public RespResult<String> add(@RequestBody SysDictData sysDictData) {
        sysDictDataService.save(sysDictData);
        return RespResult.success();
    }

    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysDictData sysDictData) {
        sysDictDataService.updateById(sysDictData);
        return RespResult.success();
    }

    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody Long[] ids) {
        sysDictDataService.removeByIds(Arrays.asList(ids));
        return RespResult.success();
    }
}

package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.model.entity.DictData;
import com.harvey.system.model.query.DictQuery;
import com.harvey.system.service.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统字典数据表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Tag(name = "字典数据Controller")
@RestController
@RequestMapping("/system/dictData")
@RequiredArgsConstructor
public class DictDataController {
    private final DictDataService dictDataService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<DictData> form(@PathVariable(value = "id") Long id) {
        DictData dictData = dictDataService.getById(id);
        return RespResult.success(dictData);
    }

    @Operation(summary = "字典数据列表")
    @PreAuthorize("@ex.hasPerm('sys:dict:data:list')")
    @GetMapping("/page")
    public RespResult<PageResult<DictData>> page(DictQuery query) {
        Page<DictData> page = dictDataService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增字典数据")
    @PreAuthorize("@ex.hasPerm('sys:dict:data:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody DictData entity) {
        dictDataService.save(entity);
        return RespResult.success();
    }

    @Operation(summary = "编辑字典数据")
    @PreAuthorize("@ex.hasPerm('sys:dict:data:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody DictData entity) {
        dictDataService.updateById(entity);
        return RespResult.success();
    }

    @Operation(summary = "删除字典数据")
    @PreAuthorize("@ex.hasPerm('sys:dict:data:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        dictDataService.removeByIds(ids);
        return RespResult.success();
    }
}

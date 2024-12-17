package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.common.base.RespResult;
import com.harvey.system.model.dto.DictDto;
import com.harvey.system.model.entity.Dict;
import com.harvey.system.model.query.DictQuery;
import com.harvey.system.model.vo.DictVO;
import com.harvey.system.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典管理 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class DictController {
    private final DictService dictService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{dictId}")
    public RespResult<Dict> form(@PathVariable(value = "dictId") Long dictId) {
        Dict sysDict = dictService.getById(dictId);
        return RespResult.success(sysDict);
    }

    @Operation(summary = "字典分页列表")
    @PreAuthorize("@ex.hasPerm('sys:dept:list')")
    @GetMapping("/page")
    public RespResult<PageResult<Dict>> page(DictQuery query) {
        Page<Dict> page = dictService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增字典")
    @PreAuthorize("@ex.hasPerm('sys:dept:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated DictDto dto) {
        dictService.saveDict(dto);
        return RespResult.success();
    }

    @Operation(summary = "编辑字典")
    @PreAuthorize("@ex.hasPerm('sys:dept:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated DictDto dto) {
        dictService.updateDict(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除字典")
    @PreAuthorize("@ex.hasPerm('sys:dept:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        dictService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "字典键值对列表")
    @GetMapping("/list")
    public RespResult<List<DictVO>> list() {
        List<DictVO> dictVOList = dictService.queryDictVOList();
        return RespResult.success(dictVOList);
    }
}

package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.domain.query.DictQueryParam;
import com.harvey.system.domain.vo.DictVO;
import com.harvey.system.entity.SysDict;
import com.harvey.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Tag(name = "字典管理Controller")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {
    private final ISysDictService sysDictService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{dictId}")
    public RespResult<SysDict> form(@PathVariable(value = "dictId") Long dictId) {
        SysDict sysDict = sysDictService.getById(dictId);
        return RespResult.success(sysDict);
    }

    @Operation(summary = "字典键值对列表")
    @GetMapping("/list")
    public RespResult<List<DictVO>> list() {
        List<DictVO> dictVOList = sysDictService.queryDictVOList();
        return RespResult.success(dictVOList);
    }

    @Operation(summary = "字典分页列表")
    @PreAuthorize("@ex.hasPerm('sys:dept:list')")
    @GetMapping("/page")
    public RespResult<PageResult<SysDict>> page(DictQueryParam queryParam) {
        Page<SysDict> page = sysDictService.queryPage(queryParam);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增字典")
    @PreAuthorize("@ex.hasPerm('sys:dept:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody SysDict sysDict) {
        sysDictService.save(sysDict);
        return RespResult.success();
    }

    @Operation(summary = "编辑字典")
    @PreAuthorize("@ex.hasPerm('sys:dept:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysDict sysDict) {
        sysDictService.updateById(sysDict);
        return RespResult.success();
    }

    @Operation(summary = "删除字典")
    @PreAuthorize("@ex.hasPerm('sys:dept:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        sysDictService.removeByIds(ids);
        return RespResult.success();
    }
}

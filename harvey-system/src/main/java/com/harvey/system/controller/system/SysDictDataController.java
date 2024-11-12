package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.domain.query.DictQueryParam;
import com.harvey.system.entity.SysDictData;
import com.harvey.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
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
    public RespResult<PageResult<SysDictData>> page(DictQueryParam queryParam) {
        String dictCode = queryParam.getDictCode();
        Page<SysDictData> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<SysDictData>()
                .eq(StringUtils.hasLength(dictCode), SysDictData::getDictCode, dictCode);
        Page<SysDictData> dictDataPage = sysDictDataService.page(page, queryWrapper);
        return RespResult.success(PageResult.of(dictDataPage));
    }

    @PreAuthorize("@ex.hasPerm('sys:dict:data:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody SysDictData sysDictData) {
        sysDictDataService.save(sysDictData);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:dict:data:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysDictData sysDictData) {
        sysDictDataService.updateById(sysDictData);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:dict:data:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        sysDictDataService.removeByIds(ids);
        return RespResult.success();
    }
}

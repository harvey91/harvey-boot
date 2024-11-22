package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.dto.LogOpDto;
import com.harvey.system.model.query.LogOpQuery;
import com.harvey.system.model.entity.LogOp;
import com.harvey.system.service.LogOpService;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志表 前端控制器
 *
 * @author harvey
 * @since 2024-11-21
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/system/log")
@RequiredArgsConstructor
public class LogOpController {
    private final LogOpService logOpService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<LogOp> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(logOpService.getById(id));
    }

    @Operation(summary = "操作日志分页列表")
    @PreAuthorize("@ex.hasPerm('sys:log:op:list')")
    @GetMapping("/page")
    public RespResult<PageResult<LogOp>> page(LogOpQuery query) {
        query.setResult(1);
        Page<LogOp> page = logOpService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "异常日志分页列表")
    @PreAuthorize("@ex.hasPerm('sys:log:ep:list')")
    @GetMapping("/exPage")
    public RespResult<PageResult<LogOp>> exPage(LogOpQuery query) {
        query.setResult(2);
        Page<LogOp> page = logOpService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:log:op:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated LogOpDto dto) {
        logOpService.saveLogOp(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:log:op:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated LogOpDto dto) {
        logOpService.updateLogOp(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:log:op:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        logOpService.deleteByIds(ids);
        return RespResult.success();
    }
}

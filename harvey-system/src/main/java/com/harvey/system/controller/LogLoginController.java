package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.dto.LogLoginDto;
import com.harvey.system.model.query.LogLoginQuery;
import com.harvey.system.model.entity.LogLogin;
import com.harvey.system.service.LogLoginService;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登陆日志表 前端控制器
 *
 * @author harvey
 * @since 2024-11-21
 */
@Tag(name = "登陆日志")
@RestController
@RequestMapping("/system/logLogin")
@RequiredArgsConstructor
public class LogLoginController {
    private final LogLoginService logLoginService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<LogLogin> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(logLoginService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:log:login:list')")
    @GetMapping("/page")
    public RespResult<PageResult<LogLogin>> page(LogLoginQuery query) {
        Page<LogLogin> page = logLoginService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:log:login:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated LogLoginDto dto) {
        logLoginService.saveLogLogin(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:log:login:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated LogLoginDto dto) {
        logLoginService.updateLogLogin(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:log:login:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        logLoginService.deleteByIds(ids);
        return RespResult.success();
    }
}

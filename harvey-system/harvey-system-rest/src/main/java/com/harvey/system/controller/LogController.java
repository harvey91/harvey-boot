package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import com.harvey.system.model.entity.LogLogin;
import com.harvey.system.model.entity.LogOp;
import com.harvey.system.model.query.LogLoginQuery;
import com.harvey.system.model.query.LogOpQuery;
import com.harvey.system.service.LogLoginService;
import com.harvey.system.service.LogOpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志管理 前端控制器
 *
 * @author harvey
 * @since 2024-11-21
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/system/log")
@RequiredArgsConstructor
public class LogController {
    private final LogOpService logOpService;
    private final LogLoginService logLoginService;

    @Operation(summary = "操作日志分页列表")
    @SaCheckPermission("sys:log:op:list")
    @GetMapping("/opPage")
    public RespResult<PageResult<LogOp>> page(LogOpQuery query) {
        query.setResult(1);
        Page<LogOp> page = logOpService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "异常日志分页列表")
    @SaCheckPermission("sys:log:ex:list")
    @GetMapping("/exPage")
    public RespResult<PageResult<LogOp>> exPage(LogOpQuery query) {
        query.setResult(2);
        Page<LogOp> page = logOpService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "登陆日志分页列表")
    @SaCheckPermission("sys:log:login:list")
    @GetMapping("/loginPage")
    public RespResult<PageResult<LogLogin>> page(LogLoginQuery query) {
        Page<LogLogin> page = logLoginService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "清空操作日志")
    @SaCheckPermission("sys:log:op:delete")
    @DeleteMapping("/delete/op")
    public RespResult<String> deleteOp() {
//        logOpService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "清空异常日志")
    @SaCheckPermission("sys:log:ex:delete")
    @DeleteMapping("/delete/ex")
    public RespResult<String> deleteEx() {
//        logOpService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "清空登陆日志")
    @SaCheckPermission("sys:log:login:delete")
    @DeleteMapping("/delete/login")
    public RespResult<String> deleteLogin() {
//        logLoginService.deleteByIds();
        return RespResult.success();
    }
}

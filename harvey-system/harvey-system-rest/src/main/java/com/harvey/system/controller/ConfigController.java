package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.result.PageResult;
import com.harvey.common.result.RespResult;
import com.harvey.system.model.dto.ConfigDto;
import com.harvey.system.model.entity.Config;
import com.harvey.system.model.query.ConfigQuery;
import com.harvey.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统配置 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Tag(name = "系统配置")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;

    @Operation(summary = "id查询表单", description = "根据配置id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<Config> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(configService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public RespResult<PageResult<Config>> page(ConfigQuery query) {
        Page<Config> dictPage = configService.queryPage(query);
        return RespResult.success(PageResult.of(dictPage));
    }

    @Operation(summary = "新增配置")
    @PreAuthorize("@ex.hasPerm('sys:config:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated ConfigDto dto) {
        configService.saveConfig(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改配置")
    @PreAuthorize("@ex.hasPerm('sys:config:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated ConfigDto dto) {
        configService.updateConfig(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除配置")
    @PreAuthorize("@ex.hasPerm('sys:config:delete')")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        configService.deleteById(id);
        return RespResult.success();
    }

    @Operation(summary = "刷新缓存")
    @PreAuthorize("@ex.hasPerm('sys:config:refresh')")
    @PatchMapping("/refresh")
    public RespResult<String> refresh() {
        configService.refresh();
        return RespResult.success();
    }
}

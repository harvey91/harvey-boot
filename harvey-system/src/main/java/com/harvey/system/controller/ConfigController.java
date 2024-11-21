package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.model.entity.Config;
import com.harvey.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Tag(name = "系统配置Controller")
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
    @Parameters({
            @Parameter(name = "pageNum", description = "页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "条数", in = ParameterIn.QUERY)
    })
    @GetMapping("/page")
    public RespResult<PageResult<Config>> page(@RequestParam("pageNum") int pageNum,
                                                  @RequestParam("pageSize") int pageSize) {
        // TODO 查询条件
        Page<Config> page = new Page<>(pageNum, pageSize);
        Page<Config> dictPage = configService.page(page);
        return RespResult.success(PageResult.of(dictPage));
    }

    @Operation(summary = "新增配置")
    @PreAuthorize("@ex.hasPerm('sys:config:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody Config sysConfig) {
        configService.save(sysConfig);
        return RespResult.success();
    }

    @Operation(summary = "修改配置")
    @PreAuthorize("@ex.hasPerm('sys:config:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody Config sysConfig) {
        configService.updateById(sysConfig);
        return RespResult.success();
    }

    @Operation(summary = "删除配置")
    @PreAuthorize("@ex.hasPerm('sys:config:delete')")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        configService.removeById(id);
        return RespResult.success();
    }

    @Operation(summary = "刷新缓存")
    @PreAuthorize("@ex.hasPerm('sys:config:refresh')")
    @PatchMapping("/refresh")
    public RespResult<String> refresh() {
        // TODO
        return RespResult.success();
    }
}

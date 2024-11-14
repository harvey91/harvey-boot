package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysConfig;
import com.harvey.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SysConfigController {
    private final ISysConfigService sysConfigService;

    @GetMapping("/form/{id}")
    public RespResult<SysConfig> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(sysConfigService.getById(id));
    }

    @GetMapping("/page")
    public RespResult<PageResult<SysConfig>> page(@RequestParam("pageNum") int pageNum,
                                                  @RequestParam("pageSize") int pageSize) {
        Page<SysConfig> page = new Page<>(pageNum, pageSize);
        Page<SysConfig> dictPage = sysConfigService.page(page);
        return RespResult.success(PageResult.of(dictPage));
    }

    @PreAuthorize("@ex.hasPerm('sys:config:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody SysConfig sysConfig) {
        sysConfigService.save(sysConfig);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:config:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysConfig sysConfig) {
        sysConfigService.updateById(sysConfig);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:config:delete')")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        sysConfigService.removeById(id);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:config:refresh')")
    @PatchMapping("/refresh")
    public RespResult<String> refresh() {

        return RespResult.success();
    }
}

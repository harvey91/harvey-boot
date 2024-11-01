package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysRole;
import com.harvey.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {
    private final ISysRoleService sysRoleService;

    @GetMapping("/form/{roleId}")
    public RespResult<SysRole> findById(@PathVariable(value = "roleId") Long roleId) {
        SysRole sysRole = sysRoleService.getById(roleId);
        return RespResult.success(sysRole);
    }

    @GetMapping("/page")
    public RespResult<PageResult<SysRole>> page(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        Page<SysRole> rolePage = sysRoleService.page(page);
        return RespResult.success(PageResult.of(rolePage));
    }

    @GetMapping("/option")
    public RespResult<Object> option() {

        return RespResult.success();
    }

    @PostMapping("/add")
    public RespResult<String> add(@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return RespResult.success();
    }

    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysRole sysRole) {
        sysRoleService.updateById(sysRole);
        return RespResult.success();
    }

    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody Long[] ids) {
        sysRoleService.removeByIds(Arrays.asList(ids));
        return RespResult.success();
    }
}

package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysUser;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-24 21:40
 **/
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/form/{id}")
    public RespResult<SysUser> formById(@PathVariable(value = "id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return RespResult.success(sysUser);
    }
    @GetMapping("/find")
    public SysUser find(String username) {
        SysUser user = sysUserService.findByUsername(username);
        return user;
    }

    @GetMapping("/me")
    public RespResult<SysUser> me() {
        SysUser user = sysUserService.findByUsername("admin");
        return RespResult.success(user);
    }

    @GetMapping("/page")
    public RespResult<PageResult<SysUser>> page(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        Page<SysUser> userPage = sysUserService.selectUserList(page);
        return RespResult.success(PageResult.of(userPage));
    }

    @PostMapping("/add")
    public RespResult<String> add(@RequestBody SysUser user) {
        if (!StringUtils.hasLength(user.getPassword())) {
            // 前端没设置密码时给默认密码
            user.setPassword("123456");
        }
        sysUserService.save(user);
        return RespResult.success();
    }

    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysUser user) {
        // 只修改指定字段
        sysUserService.updateById(user);
        return RespResult.success();
    }

    @PostMapping("/reset/password")
    public RespResult<String> resetPassword(@RequestBody SysUser user) {
        // 只修改密码字段
        sysUserService.updateById(user);
        return RespResult.success();
    }

    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody Long[] ids) {
        if (ids == null) {
            return RespResult.fail("id不能为空");
        }
        sysUserService.removeByIds(Arrays.asList(ids));
        return RespResult.success();
    }
}

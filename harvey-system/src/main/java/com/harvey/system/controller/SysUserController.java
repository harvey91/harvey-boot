package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.entity.SysUser;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/id")
    public SysUser findById(Long id) {
        return sysUserService.findById(id);
    }

    @GetMapping("/find")
    public SysUser find(String username) {
        SysUser user = sysUserService.findByUsername(username);
        return user;
    }

    @GetMapping("/list")
    public Page<SysUser> list(SysUser sysUser) {
        Page<SysUser> userList = sysUserService.selectUserList(sysUser);
        return userList;
    }

    @PostMapping("/add")
    public SysUser add(@RequestBody SysUser user) {
        int i = sysUserService.saveUser(user);
        if (i == 0) {
            // 抛出添加失败异常
        }
        return findById(user.getId());

    }

    @PostMapping("/modify")
    public SysUser modify(@RequestBody SysUser user) {
        int i = sysUserService.saveUser(user);
        if (i == 0) {
            // 抛出更新用户失败异常
        }
        return findById(user.getId());
    }

    @PostMapping("/enable")
    public int enable() {

        return 0;
    }

    @PostMapping("/delete")
    public int delete(List<Long> ids) {
        return 0;
    }
}

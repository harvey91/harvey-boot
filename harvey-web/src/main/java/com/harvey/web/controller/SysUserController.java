package com.harvey.web.controller;

import com.harvey.web.entity.SysUser;
import com.harvey.web.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-24 21:40
 **/
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/find")
    public SysUser find() {
        SysUser user = sysUserService.findByUsername("admin");
        return user;
    }

    @GetMapping("/list")
    public List<SysUser> list(SysUser sysUser) {
        List<SysUser> userList = sysUserService.selectUserList(sysUser);
        return userList;
    }
}

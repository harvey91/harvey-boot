package com.harvey.system.controller;

import com.harvey.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {
    private final ISysMenuService sysMenuService;



}

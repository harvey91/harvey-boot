package com.harvey.system.service;

import com.harvey.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<String> getPermissionByUserId(Long userId);
}

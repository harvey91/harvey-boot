package com.harvey.system.service;

import com.harvey.system.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色菜单关联表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-11-06
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    void save(Long roleId, List<Long> menuIds);
}

package com.harvey.system.service;

import com.harvey.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 系统用户角色关联表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-11-07
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    void create(Long userId, Set<Long> roleIds);

    Set<Long> getDeptIds(Long userId);
}

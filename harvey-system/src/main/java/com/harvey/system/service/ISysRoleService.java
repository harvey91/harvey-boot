package com.harvey.system.service;

import com.harvey.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
public interface ISysRoleService extends IService<SysRole> {

    Set<Long> getDeptIds(Long userId, Long deptId);
}

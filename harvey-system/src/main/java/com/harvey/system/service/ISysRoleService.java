package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.system.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
public interface ISysRoleService extends IService<SysRole> {

    List<Long> getDeptIds(Long userId, Long deptId);
}

package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.entity.SysUserRole;
import com.harvey.system.mapper.SysUserRoleMapper;
import com.harvey.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户角色关联表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-07
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    @Transactional
    public void create(Long userId, Set<Long> roleIds) {
        List<SysUserRole> userRoleList = roleIds.stream()
                .map(roleId -> SysUserRole.builder().userId(userId).roleId(roleId).build()).toList();
        saveBatch(userRoleList);
    }
}

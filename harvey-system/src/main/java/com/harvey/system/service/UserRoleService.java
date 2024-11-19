package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.UserRoleMapper;
import com.harvey.system.model.entity.UserRole;
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
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {

    @Transactional
    public void create(Long userId, Set<Long> roleIds) {
        List<UserRole> userRoleList = roleIds.stream()
                .map(roleId -> UserRole.builder().userId(userId).roleId(roleId).build()).toList();
        saveBatch(userRoleList);
    }

    public Set<Long> getDeptIds(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId).select(UserRole::getRoleId);
        List<Long> roleIdList = listObjs(wrapper);
        // TODO

        return null;
    }
}

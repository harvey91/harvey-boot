package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.system.entity.SysRole;
import com.harvey.system.entity.SysUserRole;
import com.harvey.system.enums.DataScopeEnum;
import com.harvey.system.mapper.SysRoleMapper;
import com.harvey.system.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private final SysRoleMapper mapper;

    @Override
    public Set<Long> getDeptIds(Long userId, Long deptId) {
        List<SysRole> roleList = mapper.selectRoleByUserId(userId);
        Set<Long> deptIds = new HashSet<>();
        for (SysRole role : roleList) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.get(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case DEPT:
                    // 本部门数据权限
                    deptIds.add(deptId);
                    break;
                case DEPT_CHILDREN:
                    // 本部门及子部门数据权限
                    deptIds.add(1L);
                    break;
                case PERSON:
                    // 本人数据权限，什么都不查
                    break;
                case CUSTOM:
                    // 自定义数据权限，角色关联部门
                    break;
                default:
                    return deptIds;
            }
        }

        return deptIds;
    }
}

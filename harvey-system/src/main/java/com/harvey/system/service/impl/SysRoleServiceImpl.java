package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.entity.SysDept;
import com.harvey.system.entity.SysRole;
import com.harvey.system.entity.SysRoleDept;
import com.harvey.system.enums.DataScopeEnum;
import com.harvey.system.mapper.SysRoleMapper;
import com.harvey.system.service.ISysDeptService;
import com.harvey.system.service.ISysRoleDeptService;
import com.harvey.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private final ISysRoleDeptService sysRoleDeptService;
    private final ISysDeptService sysDeptService;



    @Override
    public List<Long> getDeptIds(Long userId, Long deptId) {
        Set<Long> deptIds = new HashSet<>();
        List<SysRole> roleList = mapper.selectRoleByUserId(userId);
        for (SysRole role : roleList) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.get(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case DEPT:
                    // 本部门数据权限
                    deptIds.add(deptId);
                    break;
                case DEPT_CHILDREN:
                    // 本部门及子部门数据权限
                    deptIds.addAll(getChildDeptId(deptId));
                    break;
                case PERSON:
                    // 本人数据权限，不查任何数据
                    deptIds.add(-1L);
                    break;
                case CUSTOM:
                    // 自定义数据权限，角色关联部门
                    deptIds.addAll(getDeptByRoleId(role.getId()));
                    break;
                default:
                    return new ArrayList<>();
            }
        }

        return new ArrayList<>(deptIds);
    }

    /**
     * 获取所有子部门id
     * @param parentDeptId
     * @return
     */
    public List<Long> getChildDeptId(Long parentDeptId) {
        List<Long> deptIdList = new ArrayList<>();
        if (ObjectUtils.isEmpty(parentDeptId)) {
            // 用户没有所属部门不能查询任何数据
            deptIdList.add(-1L);
            return deptIdList;
        }
        // 查询所有部门列表，并找出参数deptId是所有子部门（包含二级、三级、、、）
        deptIdList.add(parentDeptId);
        List<SysDept> list = sysDeptService.list();
        if (!ObjectUtils.isEmpty(list)) {
            Map<Long, List<SysDept>> collect = list.stream().collect(Collectors.groupingBy(SysDept::getParentId));
            if (collect.containsKey(parentDeptId)) {
                List<SysDept> childDeptList = collect.get(parentDeptId);
                for (SysDept dept : childDeptList) {
                    deptIdList.add(dept.getId());
                    deptIdList.addAll(recursionDept(dept.getId(), collect));
                }

            }
        }
        return deptIdList;
    }

    public List<Long> recursionDept(Long parentDeptId, Map<Long, List<SysDept>> collect) {
        List<Long> deptIdList = new ArrayList<>();
        if (collect.containsKey(parentDeptId)) {
            List<SysDept> childDeptList = collect.get(parentDeptId);
            for (SysDept dept : childDeptList) {
                deptIdList.add(dept.getId());
                deptIdList.addAll(recursionDept(dept.getId(), collect));
            }

        }
        return deptIdList;
    }

    /**
     * 根据角色id获取关联的部门id
     * @param roleId
     * @return
     */
    public List<Long> getDeptByRoleId(Long roleId) {
        return sysRoleDeptService.listObjs(
                new LambdaQueryWrapper<SysRoleDept>()
                        .select(SysRoleDept::getDeptId)
                        .eq(SysRoleDept::getRoleId, roleId));
    }
}

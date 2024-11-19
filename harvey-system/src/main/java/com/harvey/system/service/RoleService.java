package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.enums.DataScopeEnum;
import com.harvey.system.mapper.RoleMapper;
import com.harvey.system.model.entity.Dept;
import com.harvey.system.model.entity.Role;
import com.harvey.system.model.entity.RoleDept;
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
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    private final RoleMapper mapper;
    private final RoleDeptService roleDeptService;
    private final DeptService deptService;

    public List<Long> getDeptIds(Long userId, Long deptId) {
        Set<Long> deptIds = new HashSet<>();
        List<Role> roleList = mapper.selectRoleByUserId(userId);
        for (Role role : roleList) {
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
        List<Dept> list = deptService.list();
        if (!ObjectUtils.isEmpty(list)) {
            Map<Long, List<Dept>> collect = list.stream().collect(Collectors.groupingBy(Dept::getParentId));
            if (collect.containsKey(parentDeptId)) {
                List<Dept> childDeptList = collect.get(parentDeptId);
                for (Dept dept : childDeptList) {
                    deptIdList.add(dept.getId());
                    deptIdList.addAll(recursionDept(dept.getId(), collect));
                }

            }
        }
        return deptIdList;
    }

    public List<Long> recursionDept(Long parentDeptId, Map<Long, List<Dept>> collect) {
        List<Long> deptIdList = new ArrayList<>();
        if (collect.containsKey(parentDeptId)) {
            List<Dept> childDeptList = collect.get(parentDeptId);
            for (Dept dept : childDeptList) {
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
        return roleDeptService.listObjs(
                new LambdaQueryWrapper<RoleDept>()
                        .select(RoleDept::getDeptId)
                        .eq(RoleDept::getRoleId, roleId));
    }
}

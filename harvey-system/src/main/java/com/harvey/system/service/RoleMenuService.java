package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.RoleMenuMapper;
import com.harvey.system.model.entity.RoleMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色菜单关联表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-06
 */
@Service
@RequiredArgsConstructor
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> {
    private final RoleMenuMapper mapper;

    @Transactional(rollbackFor = Throwable.class)
    public void save(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId);
        // roleId是否存在
        long count = mapper.selectCount(queryWrapper);
        if (count > 0) {
            // 先删除原来的关联
            mapper.delete(queryWrapper);
        }

        List<RoleMenu> sysRoleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu sysRoleMenu = new RoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        // 保存现在的关联
        saveBatch(sysRoleMenuList);

        // todo 刷新角色权限缓存
    }
}

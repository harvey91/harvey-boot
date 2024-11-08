package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.system.entity.SysRoleMenu;
import com.harvey.system.mapper.SysRoleMenuMapper;
import com.harvey.system.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
    private final SysRoleMenuMapper mapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId);
        // roleId是否存在
        long count = mapper.selectCount(queryWrapper);
        if (count > 0) {
            // 先删除原来的关联
            mapper.delete(queryWrapper);
        }

        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        // 保存现在的关联
        saveBatch(sysRoleMenuList);

        // todo 刷新角色权限缓存
    }
}

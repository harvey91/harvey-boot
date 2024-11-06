package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysRoleMenu;
import com.harvey.system.service.ISysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-11-06 16:54
 **/
@RestController
@RequestMapping("/system/role/menu")
@RequiredArgsConstructor
public class SysRoleMenuController {
    private final ISysRoleMenuService sysRoleMenuService;


    /**
     * 根据角色id获取关联的菜单id集合
     * @param roleId
     * @return
     */
    @GetMapping("/ids/{roleId}")
    public RespResult<List<Long>> getMenuIds(@PathVariable(value = "roleId") Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<SysRoleMenu>()
                .select(SysRoleMenu::getMenuId).eq(SysRoleMenu::getRoleId, roleId);
        List<Long> list = sysRoleMenuService.listObjs(queryWrapper);

        return RespResult.success(list);
    }

    @PostMapping("/ids/{roleId}")
    public RespResult<String> save(@PathVariable(value = "roleId") Long roleId,
                                   @RequestBody List<Long> menuIds) {
        if (ObjectUtils.isEmpty(menuIds)) {
            // 菜单id不能为空

        }
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId);
        // roleId是否存在
        long count = sysRoleMenuService.count(queryWrapper);
        if (count > 0) {
            // 先删除原来的关联
            sysRoleMenuService.remove(queryWrapper);
        }

        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        // 保存现在的关联
        sysRoleMenuService.saveBatch(sysRoleMenuList);

        // 刷新角色权限缓存

        return RespResult.success();
    }

}

package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysRoleMenu;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.service.ISysRoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-11-06 16:54
 **/
@Tag(name = "角色菜单关联Controller")
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
    @Operation(summary = "角色id查询菜单id集合")
    @GetMapping("/ids/{roleId}")
    public RespResult<List<Long>> getMenuIds(@PathVariable(value = "roleId") Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<SysRoleMenu>()
                .select(SysRoleMenu::getMenuId).eq(SysRoleMenu::getRoleId, roleId);
        List<Long> list = sysRoleMenuService.listObjs(queryWrapper);
        return RespResult.success(list);
    }

    @Operation(summary = "角色id绑定菜单id集合")
    @PostMapping("/ids/{roleId}")
    public RespResult<String> save(@PathVariable(value = "roleId") Long roleId,
                                   @RequestBody List<Long> menuIds) {
        if (ObjectUtils.isEmpty(menuIds)) {
            throw new BusinessException("菜单id不能为空");
        }
        sysRoleMenuService.save(roleId, menuIds);
        return RespResult.success();
    }

}

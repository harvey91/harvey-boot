package com.harvey.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.common.base.RespResult;
import com.harvey.system.model.entity.RoleMenu;
import com.harvey.system.service.RoleMenuService;
import com.harvey.common.utils.AssertUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-11-06 16:54
 **/
@Tag(name = "角色菜单")
@RestController
@RequestMapping("/system/role/menu")
@RequiredArgsConstructor
public class RoleMenuController {
    private final RoleMenuService roleMenuService;

    /**
     * 根据角色id获取关联的菜单id集合
     * @param roleId
     * @return
     */
    @Operation(summary = "角色id查询菜单id集合")
    @GetMapping("/ids/{roleId}")
    public RespResult<List<Long>> getMenuIds(@PathVariable(value = "roleId") Long roleId) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<RoleMenu>()
                .select(RoleMenu::getMenuId).eq(RoleMenu::getRoleId, roleId);
        List<Long> list = roleMenuService.listObjs(queryWrapper);
        return RespResult.success(list);
    }

    @Operation(summary = "角色id绑定菜单id集合")
    @PostMapping("/ids/{roleId}")
    public RespResult<String> save(@PathVariable(value = "roleId") Long roleId,
                                   @RequestBody List<Long> menuIds) {
        AssertUtil.isEmpty(menuIds, "菜单id不能为空");
        roleMenuService.save(roleId, menuIds);
        return RespResult.success();
    }

}

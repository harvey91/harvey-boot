package com.harvey.system.controller.system;

import com.harvey.system.base.RespResult;
import com.harvey.system.domain.query.MenuQueryParam;
import com.harvey.system.domain.vo.MenuVO;
import com.harvey.system.domain.vo.OptionVO;
import com.harvey.system.domain.vo.RouteVO;
import com.harvey.system.entity.SysMenu;
import com.harvey.system.enums.MenuTypeEnum;
import com.harvey.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Tag(name = "菜单管理Controller")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {
    private final ISysMenuService sysMenuService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{menuId}")
    public RespResult<SysMenu> form(@PathVariable("menuId") Long menuId) {
        SysMenu sysMenu = sysMenuService.getById(menuId);
        return RespResult.success(sysMenu);
    }

    @Operation(summary = "菜单列表-tree")
    @PreAuthorize("@ex.hasPerm('sys:menu:list')")
    @GetMapping("/list")
    public RespResult<List<MenuVO>> list(MenuQueryParam queryParam) {
        return RespResult.success(sysMenuService.queryList(queryParam));
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("@ex.hasPerm('sys:menu:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody SysMenu sysMenu) {
        if (MenuTypeEnum.DIRECTORY.toString().equals(sysMenu.getType())) {
            sysMenu.setComponent("Layout");
            if (!sysMenu.getRoutePath().startsWith("/")) {
                sysMenu.setRoutePath("/" + sysMenu.getRoutePath());
            }
        }
        sysMenuService.save(sysMenu);
        return RespResult.success();
    }

    @Operation(summary = "编辑菜单")
    @PreAuthorize("@ex.hasPerm('sys:menu:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return RespResult.success();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("@ex.hasPerm('sys:menu:delete')")
    @DeleteMapping("/delete/{menuId}")
    public RespResult<String> delete(@PathVariable("menuId") Long menuId) {
        sysMenuService.removeById(menuId);
        return RespResult.success();
    }

    @Operation(summary = "动态路由菜单")
    @GetMapping("/routes")
    public RespResult<List<RouteVO>> routes() {
        return RespResult.success(sysMenuService.routes());
    }

    @Operation(summary = "菜单下拉列表")
    @GetMapping("/option")
    public RespResult<List<OptionVO>> option() {
        return RespResult.success(sysMenuService.options());
    }
}

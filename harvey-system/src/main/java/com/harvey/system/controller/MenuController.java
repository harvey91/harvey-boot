package com.harvey.system.controller;

import com.harvey.system.base.RespResult;
import com.harvey.system.model.dto.MenuDto;
import com.harvey.system.model.entity.Menu;
import com.harvey.system.model.query.MenuQuery;
import com.harvey.system.model.vo.MenuVO;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.RouteVO;
import com.harvey.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{menuId}")
    public RespResult<Menu> form(@PathVariable("menuId") Long menuId) {
        Menu menu = menuService.getById(menuId);
        return RespResult.success(menu);
    }

    @Operation(summary = "菜单列表-tree")
    @PreAuthorize("@ex.hasPerm('sys:menu:list')")
    @GetMapping("/list")
    public RespResult<List<MenuVO>> list(MenuQuery query) {
        return RespResult.success(menuService.queryList(query));
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("@ex.hasPerm('sys:menu:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated MenuDto dto) {
        menuService.saveMenu(dto);
        return RespResult.success();
    }

    @Operation(summary = "编辑菜单")
    @PreAuthorize("@ex.hasPerm('sys:menu:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated MenuDto dto) {
        menuService.updateMenu(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("@ex.hasPerm('sys:menu:delete')")
    @DeleteMapping("/delete/{menuId}")
    public RespResult<String> delete(@PathVariable("menuId") Long menuId) {
        menuService.deleteById(menuId);
        return RespResult.success();
    }

    @Operation(summary = "动态路由菜单")
    @GetMapping("/routes")
    public RespResult<List<RouteVO>> routes() {
        return RespResult.success(menuService.routes());
    }

    @Operation(summary = "菜单下拉列表")
    @GetMapping("/option")
    public RespResult<List<OptionVO<Long>>> option() {
        return RespResult.success(menuService.options());
    }
}

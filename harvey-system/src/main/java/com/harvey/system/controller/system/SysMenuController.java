package com.harvey.system.controller.system;

import com.harvey.system.base.RespResult;
import com.harvey.system.domain.vo.MenuVO;
import com.harvey.system.domain.vo.OptionVO;
import com.harvey.system.domain.vo.RouteVO;
import com.harvey.system.entity.SysMenu;
import com.harvey.system.enums.MenuTypeEnum;
import com.harvey.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {
    private final ISysMenuService sysMenuService;


    @GetMapping("/form/{menuId}")
    public RespResult<SysMenu> form(@PathVariable("menuId") Long menuId) {
        SysMenu sysMenu = sysMenuService.getById(menuId);
        return RespResult.success(sysMenu);
    }

    @GetMapping("/list")
    public RespResult<List<MenuVO>> list() {
        List<SysMenu> list = sysMenuService.list();
        List<MenuVO> menuVOList = new ArrayList<>();
        Map<Long, List<SysMenu>> collect = list.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        if (collect.containsKey(0L)) {
            List<SysMenu> parentMenuList = collect.get(0L);
            for (SysMenu sysMenu : parentMenuList) {
                MenuVO menuVO = new MenuVO();
                BeanUtils.copyProperties(sysMenu, menuVO);
                menuVO.setChildren(recursionMenu(menuVO, collect));
                menuVOList.add(menuVO);
            }
        }
        return RespResult.success(menuVOList);
    }


    @PostMapping("/add")
    public RespResult<String> add(@RequestBody SysMenu sysMenu) {
        if (MenuTypeEnum.DIRECTORY.toString().equals(sysMenu.getType())) {
            sysMenu.setComponent("Layout");
            if (!sysMenu.getRoutePath().startsWith("/")) {
                sysMenu.setRoutePath("/" + sysMenu.getRoutePath());
            }
        }
        sysMenuService.save(sysMenu);
        return RespResult.success();
    }

    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return RespResult.success();
    }

    @DeleteMapping("/delete/{menuId}")
    public RespResult<String> delete(@PathVariable("menuId") Long menuId) {
        sysMenuService.removeById(menuId);
        return RespResult.success();
    }

    /**
     * 动态路由
     * @return
     */
    @GetMapping("/routes")
    public RespResult<List<RouteVO>> routes() {
        List<SysMenu> menuList = sysMenuService.list();
        Map<Long, List<SysMenu>> collect = menuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        // 获取顶级目录列表
        List<SysMenu> parentMenuList = collect.get(0L);
        List<RouteVO> routeVOList = new ArrayList<>();
        for (SysMenu menu : parentMenuList) {
            RouteVO routeVO = new RouteVO();
            routeVO.setName(menu.getRouteName());
            routeVO.setPath(menu.getRoutePath());
            routeVO.setComponent(menu.getComponent());
            routeVO.setRedirect(StringUtils.hasLength(menu.getRedirect()) ? menu.getRedirect() : null);

            RouteVO.Meta meta = new RouteVO.Meta();
            meta.setTitle(menu.getMenuName());
            meta.setIcon(menu.getIcon());
            meta.setHidden(menu.getEnabled() == 0);
            meta.setAlwaysShow(menu.getAlwaysShow() == 1);
            routeVO.setMeta(meta);
            List<RouteVO> childrenRouteVOList = recursion(menu, collect);
            routeVO.setChildren(childrenRouteVOList);
            routeVOList.add(routeVO);
        }
        return RespResult.success(routeVOList);
    }

    /**
     * 菜单下拉列表
     * @return
     */
    @GetMapping("/option")
    public RespResult<Object> option() {
        List<SysMenu> list = sysMenuService.list();
        List<OptionVO> optionVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return RespResult.success();
        }

        Map<Long, List<SysMenu>> collect = list.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        if (collect.containsKey(0L)) {
            List<SysMenu> menuList = collect.get(0L);
            for (SysMenu menu : menuList) {
                OptionVO optionVO = new OptionVO();
                optionVO.setValue(menu.getId());
                optionVO.setLabel(menu.getMenuName());
                optionVOList.add(optionVO);
                recursionOption(optionVO, collect);
            }
        }

        return RespResult.success(optionVOList);
    }

    public List<RouteVO> recursion(SysMenu parentMenu, Map<Long, List<SysMenu>> collect) {
        List<RouteVO> routeVOList = new ArrayList<>();
        if (collect.containsKey(parentMenu.getId())) {
            List<SysMenu> childMenuList = collect.get(parentMenu.getId());
            for (SysMenu menu : childMenuList) {
                RouteVO routeVO = new RouteVO();
                routeVO.setName(menu.getRouteName());
                routeVO.setPath(menu.getRoutePath());
                routeVO.setComponent(menu.getComponent());
                routeVO.setRedirect(StringUtils.hasLength(menu.getRedirect()) ? menu.getRedirect() : null);

                RouteVO.Meta meta = new RouteVO.Meta();
                meta.setTitle(menu.getMenuName());
                meta.setIcon(menu.getIcon());
                meta.setHidden(menu.getEnabled() == 0);
                meta.setAlwaysShow(menu.getAlwaysShow() == 1);
                meta.setKeepAlive(menu.getKeepAlive() == 1);
                routeVO.setMeta(meta);

                routeVOList.add(routeVO);
                recursion(menu, collect);
            }
        }
        return routeVOList;
    }

    /**
     * 递归所有下级菜单-下拉列表
     * @param parentOptionVO
     * @param collect
     */
    public void recursionOption(OptionVO parentOptionVO, Map<Long, List<SysMenu>> collect) {
        if (!collect.containsKey(parentOptionVO.getValue())) {
            return;
        }
        List<OptionVO> optionVOList = new ArrayList<>();
        List<SysMenu> childMenuList = collect.get(parentOptionVO.getValue());
        for (SysMenu menu : childMenuList) {
            OptionVO optionVO = new OptionVO();
            optionVO.setValue(menu.getId());
            optionVO.setLabel(menu.getMenuName());
            optionVOList.add(optionVO);
            recursionOption(optionVO, collect);
        }
        parentOptionVO.setChildren(optionVOList);
    }

    /**
     * 递归所有下级菜单-列表
     * @param parentMenuVO
     * @param collect
     */
    public List<MenuVO> recursionMenu(MenuVO parentMenuVO, Map<Long, List<SysMenu>> collect) {
        List<MenuVO> menuVOList = new ArrayList<>();
        if (collect.containsKey(parentMenuVO.getId())) {
            List<SysMenu> childMenuList = collect.get(parentMenuVO.getId());
            for (SysMenu menu : childMenuList) {
                MenuVO menuVO = new MenuVO();
                BeanUtils.copyProperties(menu, menuVO);
                menuVOList.add(menuVO);
                menuVO.setChildren(recursionMenu(menuVO, collect));
            }
        }
        return menuVOList;
    }

}

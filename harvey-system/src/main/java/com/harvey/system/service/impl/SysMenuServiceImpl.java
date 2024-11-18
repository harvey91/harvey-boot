package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.domain.query.MenuQueryParam;
import com.harvey.system.domain.vo.MenuVO;
import com.harvey.system.domain.vo.OptionVO;
import com.harvey.system.domain.vo.RouteVO;
import com.harvey.system.entity.SysMenu;
import com.harvey.system.mapper.SysMenuMapper;
import com.harvey.system.service.ISysMenuService;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    private final SysMenuMapper mapper;

    /**
     * 所有菜单列表-树形结构
     * @return
     */
    @Override
    public List<MenuVO> queryList(MenuQueryParam queryParam) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), SysMenu::getMenuName, queryParam.getKeywords())
                .orderByAsc(SysMenu::getSort);
        List<SysMenu> list = mapper.selectList(queryWrapper);
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
        return menuVOList;
    }

    /**
     * 动态路由
     * @return
     */
    @Override
    public List<RouteVO> routes() {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort);
        List<SysMenu> menuList = mapper.selectList(queryWrapper);
        Map<Long, List<SysMenu>> collect = menuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        // 获取顶级目录列表
        List<SysMenu> parentMenuList = collect.get(0L);
        List<RouteVO> routeVOList = new ArrayList<>();
        for (SysMenu menu : parentMenuList) {
            RouteVO routeVO = new RouteVO();
            routeVO.setName(menu.getRouteName());
            routeVO.setPath(menu.getRoutePath());
            routeVO.setComponent(menu.getComponent());
            routeVO.setRedirect(org.springframework.util.StringUtils.hasLength(menu.getRedirect()) ? menu.getRedirect() : null);

            RouteVO.Meta meta = new RouteVO.Meta();
            meta.setTitle(menu.getMenuName());
            meta.setIcon(menu.getIcon());
            meta.setHidden(menu.getEnabled() == 0);
            meta.setAlwaysShow(menu.getAlwaysShow() == 1);
            routeVO.setMeta(meta);
            List<RouteVO> childrenRouteVOList = recursionRoute(menu, collect);
            routeVO.setChildren(childrenRouteVOList);
            routeVOList.add(routeVO);
        }
        return routeVOList;
    }

    /**
     * 菜单下拉列表-树形
     * @return
     */
    @Override
    public List<OptionVO> options() {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort);
        List<SysMenu> menuList = mapper.selectList(queryWrapper);
        List<OptionVO> optionVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(menuList)) {
            return Collections.emptyList();
        }

        Map<Long, List<SysMenu>> collect = menuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        if (collect.containsKey(0L)) {
            List<SysMenu> parentMenuList = collect.get(0L);
            for (SysMenu menu : parentMenuList) {
                OptionVO optionVO = new OptionVO();
                optionVO.setValue(menu.getId());
                optionVO.setLabel(menu.getMenuName());
                optionVOList.add(optionVO);
                recursionOption(optionVO, collect);
            }
        }
        return optionVOList;
    }

    @Override
    public List<String> getPermissionByUserId(Long userId) {
        List<SysMenu> menuList = mapper.selectMenuByUserId(userId);
        return menuList.stream().map(SysMenu::getPermission).filter(StringUtils::isNotBlank).toList();
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

    /**
     * 递归所有下级菜单-动态路由
     * @param parentMenu 上级菜单
     * @param collect 所有菜单分组
     * @return
     */
    public List<RouteVO> recursionRoute(SysMenu parentMenu, Map<Long, List<SysMenu>> collect) {
        List<RouteVO> routeVOList = new ArrayList<>();
        if (collect.containsKey(parentMenu.getId())) {
            List<SysMenu> childMenuList = collect.get(parentMenu.getId());
            for (SysMenu menu : childMenuList) {
                RouteVO routeVO = new RouteVO();
                routeVO.setName(menu.getRouteName());
                routeVO.setPath(menu.getRoutePath());
                routeVO.setComponent(menu.getComponent());
                routeVO.setRedirect(org.springframework.util.StringUtils.hasLength(menu.getRedirect()) ? menu.getRedirect() : null);

                RouteVO.Meta meta = new RouteVO.Meta();
                meta.setTitle(menu.getMenuName());
                meta.setIcon(menu.getIcon());
                meta.setHidden(menu.getEnabled() == 0);
                meta.setAlwaysShow(menu.getAlwaysShow() == 1);
                meta.setKeepAlive(menu.getKeepAlive() == 1);
                routeVO.setMeta(meta);

                routeVOList.add(routeVO);
                recursionRoute(menu, collect);
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
}

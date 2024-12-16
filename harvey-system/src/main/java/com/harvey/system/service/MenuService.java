package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.constant.CacheConstant;
import com.harvey.system.enums.MenuTypeEnum;
import com.harvey.system.mapper.MenuMapper;
import com.harvey.system.mapstruct.MenuConverter;
import com.harvey.system.model.dto.MenuDto;
import com.harvey.system.model.entity.Menu;
import com.harvey.system.model.query.MenuQuery;
import com.harvey.system.model.vo.MenuVO;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.RouteVO;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Service
@RequiredArgsConstructor
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    private final MenuMapper mapper;
    private final MenuConverter converter;

    public Page<Menu> queryPage(MenuQuery query) {
        Page<Menu> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Menu::getMenuName, query.getKeywords())
                .orderByAsc(Menu::getSort);
        return this.page(page, queryWrapper);
    }

    @CacheEvict(value = CacheConstant.MENU_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void saveMenu(MenuDto dto) {
        Menu entity = converter.toEntity(dto);
        if (MenuTypeEnum.DIRECTORY.toString().equals(entity.getType())) {
            entity.setComponent("Layout");
            if (!entity.getRoutePath().startsWith("/")) {
                entity.setRoutePath("/" + entity.getRoutePath());
            }
        }
        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }
        this.save(entity);
    }

    @CacheEvict(value = CacheConstant.MENU_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void updateMenu(MenuDto dto) {
        Menu entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @CacheEvict(value = CacheConstant.MENU_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }

    @CacheEvict(value = CacheConstant.MENU_KEY, key = "'all'")
    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        this.removeById(id);
    }

    /**
     * 所有菜单列表-树形结构
     * @return
     */
    public List<MenuVO> queryList(MenuQuery query) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Menu::getMenuName, query.getKeywords())
                .orderByAsc(Menu::getSort);
        List<Menu> list = mapper.selectList(queryWrapper);
        List<MenuVO> menuVOList = new ArrayList<>();
        Map<Long, List<Menu>> collect = list.stream().collect(Collectors.groupingBy(Menu::getParentId));
        if (collect.containsKey(0L)) {
            List<Menu> parentMenuList = collect.get(0L);
            for (Menu menu : parentMenuList) {
                MenuVO menuVO = converter.toVO(menu);
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
    @Cacheable(value = CacheConstant.MENU_KEY, key = "'all'")
    public List<RouteVO> routes() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<Menu>().orderByAsc(Menu::getSort);
        // TODO 获取当前用户的菜单列表
        List<Menu> menuList = mapper.selectList(queryWrapper);
        Map<Long, List<Menu>> collect = menuList.stream().collect(Collectors.groupingBy(Menu::getParentId));
        // 获取顶级目录列表
        List<Menu> parentMenuList = collect.get(0L);
        List<RouteVO> routeVOList = new ArrayList<>();
        for (Menu menu : parentMenuList) {
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
    public List<OptionVO<Long>> options() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<Menu>().orderByAsc(Menu::getSort);
        List<Menu> menuList = mapper.selectList(queryWrapper);
        List<OptionVO<Long>> optionVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(menuList)) {
            return Collections.emptyList();
        }

        Map<Long, List<Menu>> collect = menuList.stream().collect(Collectors.groupingBy(Menu::getParentId));
        if (collect.containsKey(0L)) {
            List<Menu> parentMenuList = collect.get(0L);
            for (Menu menu : parentMenuList) {
                OptionVO<Long> optionVO = OptionVO.<Long>builder().value(menu.getId()).label(menu.getMenuName()).build();
                optionVOList.add(optionVO);
                recursionOption(optionVO, collect);
            }
        }
        return optionVOList;
    }

    public List<String> getPermissionByUserId(Long userId) {
        List<Menu> menuList = mapper.selectMenuByUserId(userId);
        return menuList.stream().map(Menu::getPermission).filter(StringUtils::isNotBlank).toList();
    }

    /**
     * 递归所有下级菜单-列表
     * @param parentMenuVO
     * @param collect
     */
    public List<MenuVO> recursionMenu(MenuVO parentMenuVO, Map<Long, List<Menu>> collect) {
        List<MenuVO> menuVOList = new ArrayList<>();
        if (collect.containsKey(parentMenuVO.getId())) {
            List<Menu> childMenuList = collect.get(parentMenuVO.getId());
            for (Menu menu : childMenuList) {
                MenuVO menuVO = converter.toVO(menu);
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
    public List<RouteVO> recursionRoute(Menu parentMenu, Map<Long, List<Menu>> collect) {
        List<RouteVO> routeVOList = new ArrayList<>();
        if (collect.containsKey(parentMenu.getId())) {
            List<Menu> childMenuList = collect.get(parentMenu.getId());
            for (Menu menu : childMenuList) {
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
    public void recursionOption(OptionVO<Long> parentOptionVO, Map<Long, List<Menu>> collect) {
        if (!collect.containsKey(parentOptionVO.getValue())) {
            return;
        }
        List<OptionVO<Long>> optionVOList = new ArrayList<>();
        List<Menu> childMenuList = collect.get(parentOptionVO.getValue());
        for (Menu menu : childMenuList) {
            OptionVO<Long> optionVO = OptionVO.<Long>builder().value(menu.getId()).label(menu.getMenuName()).build();
            optionVOList.add(optionVO);
            recursionOption(optionVO, collect);
        }
        parentOptionVO.setChildren(optionVOList);
    }
}

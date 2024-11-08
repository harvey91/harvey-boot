package com.harvey.system.service.impl;

import com.harvey.system.entity.SysMenu;
import com.harvey.system.mapper.SysMenuMapper;
import com.harvey.system.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<String> getPermissionByUserId(Long userId) {
        List<SysMenu> menuList = mapper.selectMenuByUserId(userId);
        return menuList.stream().map(SysMenu::getPermission).toList();
    }
}

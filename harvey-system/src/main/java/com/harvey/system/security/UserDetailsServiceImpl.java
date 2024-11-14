package com.harvey.system.security;

import com.harvey.system.entity.SysUser;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.service.ISysMenuService;
import com.harvey.system.service.ISysRoleService;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Harvey
 * @date 2024-11-04 10:53
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserService sysUserService;
    private final ISysRoleService sysRoleService;
    private final ISysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserService.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("");
        } else if (user.getEnabled() == 0) {
            // 账号被禁用
            throw new BusinessException("账号未激活，请联系管理员!");
        }
        // 登陆时把当前用户的数据权限对应的deptId集合查出来，后续所有含有deptId的表格查询都通过deptId过滤
        List<Long> deptIds = sysRoleService.getDeptIds(user.getId(), user.getDeptId());
        // 用户菜单权限列表
        List<String> permissions = sysMenuService.getPermissionByUserId(user.getId());
        log.debug("permissions list: {}", permissions);
        // 用户角色列表 TODO
        List<SimpleGrantedAuthority> authorities = permissions.stream().map(SimpleGrantedAuthority::new).toList();
        return LoginUserVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .isAdmin(user.getId() == 1L)
                .deptId(user.getDeptId())
                .enabled(Objects.equals(user.getEnabled(), 1))
                .dataScopes(deptIds)
                .permissions(permissions)
                .authorities(authorities)
                .build();
    }
}

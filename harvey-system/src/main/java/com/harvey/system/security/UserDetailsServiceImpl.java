package com.harvey.system.security;

import com.harvey.system.entity.SysUser;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Harvey
 * @date 2024-11-04 10:53
 **/
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserService.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("");
        } else if (user.getEnabled() == 0) {
            // 账号被禁用
        }
        return User.builder().username(username).password(user.getPassword()).build();
    }
}

package com.harvey.system.security.service;

import com.harvey.system.enums.LoginResultEnum;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.model.entity.User;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.service.LogService;
import com.harvey.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Harvey
 * @date 2024-11-04 10:53
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final LogService logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (Objects.isNull(user)) {
            logService.saveLoginLog(0L, username, LoginResultEnum.LOGIN_FAILED.getValue(), "用户名不存在");
            throw new UsernameNotFoundException("用户名或密码错误");
        } else if (user.getEnabled() == 0) {
            // 账号被禁用
            logService.saveLoginLog(0L, username, LoginResultEnum.LOGIN_FAILED.getValue(), "账号被禁用");
            throw new BusinessException("账号未激活，请联系管理员!");
        }

        return LoginUserVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .isAdmin(user.getId() == 1L)
                .deptId(user.getDeptId())
                .enabled(Objects.equals(user.getEnabled(), 1))
                .build();
    }
}

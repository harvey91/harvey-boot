package com.harvey.system.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * @author Harvey
 * @date 2024-11-06 13:40
 **/
public class SecurityUtil {

    public static Long getUserId() {
        return getLoginUserVO().map(LoginUserVO::getUserId).orElse(0L);
    }

    public static String getUuid() {
        return getLoginUserVO().map(LoginUserVO::getUuid).orElse("");
    }

    public static Optional<LoginUserVO> getLoginUserVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication)) {
            return Optional.empty();
        }
        return Optional.of((LoginUserVO) authentication.getPrincipal());
    }
}

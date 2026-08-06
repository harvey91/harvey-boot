package com.harvey.system.security;

import cn.dev33.satoken.stp.StpUtil;
import com.harvey.common.constant.CacheConstant;

import java.util.Optional;

/**
 * @author Harvey
 * @date 2024-11-06 13:40
 **/
public class SecurityUtil {

    public static Long getUserId() {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            return 0L;
        }
    }

    public static String getUuid() {
        return getLoginUserVO().map(LoginUserVO::getUuid).orElse("");
    }

    public static Optional<LoginUserVO> getLoginUserVO() {
        if (!StpUtil.isLogin()) {
            return Optional.empty();
        }
        Object obj = StpUtil.getSession().get(CacheConstant.LOGIN_USER_KEY);
        if (obj instanceof LoginUserVO loginUserVO) {
            return Optional.of(loginUserVO);
        }
        return Optional.empty();
    }
}

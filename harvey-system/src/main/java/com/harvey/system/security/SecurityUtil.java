package com.harvey.system.security;

import com.harvey.system.base.RespResult;
import com.harvey.system.enums.ErrorCodeEnum;
import com.harvey.system.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

/**
 * @author Harvey
 * @date 2024-11-06 13:40
 **/
public class SecurityUtil {

    public static Long getUserId() {
        LoginUserVO loginUserVO = getLoginUserVO();
        if (ObjectUtils.isEmpty(loginUserVO)) {
            return 0L;
        }
        return loginUserVO.getUserId();
    }

    public static String getUuid() {
        LoginUserVO loginUserVO = getLoginUserVO();
        if (ObjectUtils.isEmpty(loginUserVO)) {
            return "";
        }
        return loginUserVO.getUuid();
    }

    public static LoginUserVO getLoginUserVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication)) {
            return null;
        }
        return (LoginUserVO) authentication.getPrincipal();
    }
}

package com.harvey.system.security.config;

import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 权限校验
 * 可在controller中的方法上使用@PreAuthorize("ex.hasPerm('sys:user:add')")
 *
 * @author Harvey
 * @date 2024-11-12 10:34
 **/
@Component("ex")
public class AuthorityConfig {

    public boolean hasPerm(String permission) {
        if (SecurityUtil.getLoginUserVO().isEmpty()) {
            return false;
        }
        // 获取当前登录用户认证信息
        LoginUserVO loginUserVO = SecurityUtil.getLoginUserVO().get();
        List<String> permissions = loginUserVO.getPermissions();
        // TODO 判断当前用户是否为管理员角色，管理员角色可直接放行
        // 判断登录用户权限列表中是否包含参数permission
        return loginUserVO.getIsAdmin() || permissions.contains(permission);
    }
}

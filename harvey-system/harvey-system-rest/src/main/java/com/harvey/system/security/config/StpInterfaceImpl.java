package com.harvey.system.security.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.harvey.common.constant.CacheConstant;
import com.harvey.system.security.LoginUserVO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * sa-token 权限/角色加载接口
 * 登录时将权限、角色快照到 SaSession，这里直接从会话读取
 *
 * @author Harvey
 * @date 2024-11-12 10:34
 **/
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合，超级管理员（id=1）返回 "*" 表示拥有全部权限
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUserVO loginUserVO = getLoginUser(loginId);
        if (loginUserVO == null) {
            return Collections.emptyList();
        }
        if (Boolean.TRUE.equals(loginUserVO.getIsAdmin())) {
            return List.of("*");
        }
        return loginUserVO.getPermissions();
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUserVO loginUserVO = getLoginUser(loginId);
        if (loginUserVO == null) {
            return Collections.emptyList();
        }
        return loginUserVO.getRoles();
    }

    private LoginUserVO getLoginUser(Object loginId) {
        Object obj = StpUtil.getSessionByLoginId(loginId).get(CacheConstant.LOGIN_USER_KEY);
        return obj instanceof LoginUserVO loginUserVO ? loginUserVO : null;
    }
}

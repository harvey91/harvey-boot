package com.harvey.screen.tcp.auth;

import cn.hutool.core.util.StrUtil;
import com.harvey.screen.api.LoginMessage;
import com.harvey.screen.tcp.property.ScreenNettyProperties;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认设备登录鉴权实现(骨架阶段)：
 * <ul>
 *   <li>auth.required=false 时直接放行</li>
 *   <li>token 白名单为空时允许任意非空 token</li>
 *   <li>token 白名单非空时仅允许白名单内的 token</li>
 * </ul>
 *
 * @author Harvey
 */
@Slf4j
@Component
public class DefaultScreenDeviceAuthenticator implements ScreenDeviceAuthenticator {

    private final ScreenNettyProperties properties;

    public DefaultScreenDeviceAuthenticator(ScreenNettyProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean authenticate(LoginMessage login, ChannelHandlerContext ctx) {
        ScreenNettyProperties.Auth auth = properties.getAuth();
        if (!auth.isRequired()) {
            return true;
        }
        if (login == null || StrUtil.isBlank(login.getDeviceNo()) || StrUtil.isBlank(login.getToken())) {
            log.warn("设备登录参数不完整, 拒绝: deviceNo={}, token={}, remote={}",
                    login == null ? null : login.getDeviceNo(),
                    login == null ? null : login.getToken(),
                    ctx.channel().remoteAddress());
            return false;
        }
        List<String> whitelist = auth.getTokenWhitelist();
        if (!whitelist.isEmpty() && !whitelist.contains(login.getToken())) {
            log.warn("设备登录 token 不在白名单, 拒绝: deviceNo={}, remote={}", login.getDeviceNo(), ctx.channel().remoteAddress());
            return false;
        }
        return true;
    }
}
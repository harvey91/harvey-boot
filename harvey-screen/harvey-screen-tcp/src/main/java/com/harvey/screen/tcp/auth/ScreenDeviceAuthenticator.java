package com.harvey.screen.tcp.auth;

import com.harvey.screen.api.LoginMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 设备登录鉴权接口
 * <p>
 * 骨架阶段使用默认实现(令牌白名单)，后续可接入 Redis/DB 实时校验。
 *
 * @author Harvey
 */
public interface ScreenDeviceAuthenticator {

    /**
     * 校验设备登录请求
     *
     * @param login 登录消息体
     * @param ctx   通道上下文
     * @return true=允许登录
     */
    boolean authenticate(LoginMessage login, ChannelHandlerContext ctx);
}
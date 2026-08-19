package com.harvey.screen.tcp.handler.impl;

import com.harvey.screen.api.AckMessage;
import com.harvey.screen.api.AckStatus;
import com.harvey.screen.api.LoginMessage;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import com.harvey.screen.tcp.ScreenChannelAttributes;
import com.harvey.screen.tcp.auth.ScreenDeviceAuthenticator;
import com.harvey.screen.tcp.handler.ScreenMessageHandler;
import com.harvey.screen.tcp.listener.ScreenSessionListener;
import com.harvey.screen.tcp.session.ScreenSession;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import com.harvey.screen.tcp.support.ScreenSeqGenerator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 设备登录处理：鉴权 -> 注册会话 -> 应答，成功后取消登录超时任务
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenLoginMessageHandler implements ScreenMessageHandler {

    private final ScreenDeviceAuthenticator authenticator;
    private final ScreenSessionManager sessionManager;
    private final ScreenSeqGenerator seqGenerator;
    private final List<ScreenSessionListener> sessionListeners;

    public ScreenLoginMessageHandler(ScreenDeviceAuthenticator authenticator,
                                     ScreenSessionManager sessionManager,
                                     ScreenSeqGenerator seqGenerator,
                                     List<ScreenSessionListener> sessionListeners) {
        this.authenticator = authenticator;
        this.sessionManager = sessionManager;
        this.seqGenerator = seqGenerator;
        this.sessionListeners = sessionListeners;
    }

    @Override
    public byte type() {
        return ScreenMessageType.LOGIN.getCode();
    }

    @Override
    public void handle(ScreenMessage message, ChannelHandlerContext ctx) {
        LoginMessage login = message.payload(LoginMessage.class);
        Channel channel = ctx.channel();

        if (!authenticator.authenticate(login, ctx)) {
            ctx.writeAndFlush(ScreenMessage.ack(seqGenerator.next(), AckMessage.of(message.getSeq(), AckStatus.UNAUTHORIZED, "认证失败")));
            ctx.close();
            return;
        }

        String deviceNo = login.getDeviceNo();
        channel.attr(ScreenChannelAttributes.DEVICE_NO).set(deviceNo);
        channel.attr(ScreenChannelAttributes.LOGIN_TIME).set(System.currentTimeMillis());

        String remote = String.valueOf(channel.remoteAddress());
        ScreenSession session = new ScreenSession(deviceNo, channel, login.getToken(),
                System.currentTimeMillis(), System.currentTimeMillis(),
                remote, resolveIp(channel), login.getModel());
        sessionManager.register(session);

        ScheduledFuture<?> task = channel.attr(ScreenChannelAttributes.LOGIN_TIMEOUT_TASK).get();
        if (task != null) {
            task.cancel(false);
            channel.attr(ScreenChannelAttributes.LOGIN_TIMEOUT_TASK).set(null);
        }

        log.info("设备登录成功: deviceNo={}, model={}, remote={}, 当前在线={}",
                deviceNo, login.getModel(), remote, sessionManager.onlineCount());

        for (ScreenSessionListener listener : sessionListeners) {
            try {
                listener.onLogin(session);
            } catch (Exception e) {
                log.error("设备上线监听器处理失败: deviceNo={}", deviceNo, e);
            }
        }
        ctx.writeAndFlush(ScreenMessage.ack(seqGenerator.next(), AckMessage.of(message.getSeq(), AckStatus.OK, "登录成功")));
    }

    private String resolveIp(Channel channel) {
        if (channel.remoteAddress() instanceof InetSocketAddress address) {
            return address.getAddress().getHostAddress();
        }
        return String.valueOf(channel.remoteAddress());
    }
}
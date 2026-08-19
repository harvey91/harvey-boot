package com.harvey.screen.tcp.handler;

import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.tcp.ScreenChannelAttributes;
import com.harvey.screen.tcp.listener.ScreenSessionListener;
import com.harvey.screen.tcp.property.ScreenNettyProperties;
import com.harvey.screen.tcp.session.ScreenSession;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 信发服务端核心处理器：
 * <ul>
 *   <li>连接建立后启动登录超时检测</li>
 *   <li>按消息类型分发到对应 {@link ScreenMessageHandler}</li>
 *   <li>空闲(心跳超时)断开连接</li>
 *   <li>连接断开时注销会话</li>
 * </ul>
 *
 * @author Harvey
 */
@Slf4j
public class ScreenServerHandler extends ChannelInboundHandlerAdapter {

    private final ScreenNettyProperties properties;
    private final ScreenSessionManager sessionManager;
    private final List<ScreenSessionListener> sessionListeners;
    private final Map<Byte, ScreenMessageHandler> handlerMap;

    public ScreenServerHandler(ScreenNettyProperties properties,
                               ScreenSessionManager sessionManager,
                               List<ScreenSessionListener> sessionListeners,
                               List<ScreenMessageHandler> handlers) {
        this.properties = properties;
        this.sessionManager = sessionManager;
        this.sessionListeners = sessionListeners;
        this.handlerMap = handlers.stream().collect(Collectors.toMap(ScreenMessageHandler::type, handler -> handler));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ScheduledFuture<?> task = ctx.executor().schedule(() -> {
            if (ctx.channel().attr(ScreenChannelAttributes.DEVICE_NO).get() == null) {
                log.warn("设备登录超时, 关闭连接: remote={}", ctx.channel().remoteAddress());
                ctx.close();
            }
        }, properties.getLoginTimeoutSeconds(), TimeUnit.SECONDS);
        ctx.channel().attr(ScreenChannelAttributes.LOGIN_TIMEOUT_TASK).set(task);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof ScreenMessage screenMessage)) {
            ctx.fireChannelRead(msg);
            return;
        }
        ScreenMessageHandler handler = handlerMap.get(screenMessage.getMessageType());
        if (handler == null) {
            log.warn("未知消息类型, 忽略: type={}, remote={}", screenMessage.getMessageType(), ctx.channel().remoteAddress());
            return;
        }
        try {
            handler.handle(screenMessage, ctx);
        } catch (Exception e) {
            log.error("处理信发消息失败, 关闭连接: type={}, remote={}", screenMessage.getMessageType(), ctx.channel().remoteAddress(), e);
            ctx.close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            log.warn("连接空闲(心跳超时), 关闭连接: remote={}, state={}", ctx.channel().remoteAddress(), ((IdleStateEvent) evt).state());
            ctx.close();
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ScreenSession session = sessionManager.getByChannel(ctx.channel());
        sessionManager.unregister(ctx.channel());
        if (session != null) {
            for (ScreenSessionListener listener : sessionListeners) {
                try {
                    listener.onLogout(session);
                } catch (Exception e) {
                    log.error("设备下线监听器处理失败: deviceNo={}", session.getDeviceNo(), e);
                }
            }
        }
        ctx.fireChannelInactive();
    }
}
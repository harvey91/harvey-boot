package com.harvey.screen.tcp.handler.impl;

import cn.hutool.core.util.StrUtil;
import com.harvey.screen.api.AckMessage;
import com.harvey.screen.api.AckStatus;
import com.harvey.screen.api.HeartbeatMessage;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import com.harvey.screen.tcp.ScreenChannelAttributes;
import com.harvey.screen.tcp.handler.ScreenMessageHandler;
import com.harvey.screen.tcp.listener.ScreenSessionListener;
import com.harvey.screen.tcp.session.ScreenSession;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import com.harvey.screen.tcp.support.ScreenSeqGenerator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设备心跳处理：校验设备号 -> 更新心跳时间 -> 通知会话监听器 -> 应答
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenHeartbeatMessageHandler implements ScreenMessageHandler {

    private final ScreenSessionManager sessionManager;
    private final ScreenSeqGenerator seqGenerator;
    private final List<ScreenSessionListener> sessionListeners;

    public ScreenHeartbeatMessageHandler(ScreenSessionManager sessionManager, ScreenSeqGenerator seqGenerator,
                                         List<ScreenSessionListener> sessionListeners) {
        this.sessionManager = sessionManager;
        this.seqGenerator = seqGenerator;
        this.sessionListeners = sessionListeners;
    }

    @Override
    public byte type() {
        return ScreenMessageType.HEARTBEAT.getCode();
    }

    @Override
    public void handle(ScreenMessage message, ChannelHandlerContext ctx) {
        HeartbeatMessage heartbeat = message.payload(HeartbeatMessage.class);
        Channel channel = ctx.channel();
        String deviceNo = channel.attr(ScreenChannelAttributes.DEVICE_NO).get();
        if (StrUtil.isBlank(deviceNo) || heartbeat == null || !deviceNo.equals(heartbeat.getDeviceNo())) {
            log.warn("心跳设备号不匹配, 关闭连接: remote={}, deviceNo={}, heartbeat={}", channel.remoteAddress(), deviceNo, heartbeat == null ? null : heartbeat.getDeviceNo());
            ctx.close();
            return;
        }
        sessionManager.updateHeartbeat(channel);
        ScreenSession session = sessionManager.get(deviceNo);
        if (session != null) {
            for (ScreenSessionListener listener : sessionListeners) {
                try {
                    listener.onHeartbeat(session);
                } catch (Exception e) {
                    log.error("心跳监听器执行异常: deviceNo={}, listener={}", deviceNo, listener.getClass().getSimpleName(), e);
                }
            }
        }
        ctx.writeAndFlush(ScreenMessage.ack(seqGenerator.next(), AckMessage.of(message.getSeq(), AckStatus.OK, "ok")));
    }
}
package com.harvey.screen.tcp.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 设备会话管理器：维护 deviceNo -> 会话 与 channelId -> deviceNo 双向索引
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenSessionManager {

    private final ConcurrentMap<String, ScreenSession> sessions = new ConcurrentHashMap<>();

    private final ConcurrentMap<ChannelId, String> channelIndex = new ConcurrentHashMap<>();

    public boolean isOnline(String deviceNo) {
        return sessions.containsKey(deviceNo);
    }

    public int onlineCount() {
        return sessions.size();
    }

    public Set<String> onlineDeviceNos() {
        return sessions.keySet();
    }

    public ScreenSession get(String deviceNo) {
        return sessions.get(deviceNo);
    }

    public ScreenSession getByChannel(Channel channel) {
        String deviceNo = channelIndex.get(channel.id());
        return deviceNo == null ? null : sessions.get(deviceNo);
    }

    /**
     * 注册会话；同一设备重复登录时踢掉旧连接，保证唯一会话
     */
    public void register(ScreenSession session) {
        Channel channel = session.getChannel();
        ScreenSession old = sessions.put(session.getDeviceNo(), session);
        if (old != null && old.getChannel() != channel && old.getChannel().isActive()) {
            log.warn("设备重复登录, 踢掉旧连接: deviceNo={}, remote={}", session.getDeviceNo(), old.getRemoteAddress());
            old.getChannel().close();
        }
        channelIndex.put(channel.id(), session.getDeviceNo());
    }

    /**
     * 注销会话(连接断开时调用)
     */
    public void unregister(Channel channel) {
        String deviceNo = channelIndex.remove(channel.id());
        if (deviceNo != null) {
            ScreenSession session = sessions.get(deviceNo);
            if (session != null && session.getChannel() == channel) {
                sessions.remove(deviceNo);
            }
            log.info("设备离线: deviceNo={}, 当前在线={}", deviceNo, sessions.size());
        }
    }

    /**
     * 更新最近心跳时间
     */
    public void updateHeartbeat(Channel channel) {
        String deviceNo = channelIndex.get(channel.id());
        if (deviceNo != null) {
            ScreenSession session = sessions.get(deviceNo);
            if (session != null) {
                session.setLastHeartbeatTime(System.currentTimeMillis());
            }
        }
    }
}
package com.harvey.screen.tcp.listener;

import com.harvey.screen.tcp.session.ScreenSession;

/**
 * 设备会话监听器
 * <p>
 * 由服务模块实现，用于设备上下线时的状态持久化(最后在线时间/IP/在线状态)。
 *
 * @author Harvey
 */
public interface ScreenSessionListener {

    default void onLogin(ScreenSession session) {
    }

    default void onHeartbeat(ScreenSession session) {
    }

    default void onLogout(ScreenSession session) {
    }
}
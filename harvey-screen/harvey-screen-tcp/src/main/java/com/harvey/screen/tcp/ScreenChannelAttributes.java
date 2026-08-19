package com.harvey.screen.tcp;

import io.netty.util.AttributeKey;

import java.util.concurrent.ScheduledFuture;

/**
 * Channel 属性键
 *
 * @author Harvey
 */
public final class ScreenChannelAttributes {

    /** 已登录的设备编号 */
    public static final AttributeKey<String> DEVICE_NO = AttributeKey.valueOf("screen.deviceNo");

    /** 登录时间 */
    public static final AttributeKey<Long> LOGIN_TIME = AttributeKey.valueOf("screen.loginTime");

    /** 登录超时任务(登录成功后取消) */
    public static final AttributeKey<ScheduledFuture<?>> LOGIN_TIMEOUT_TASK = AttributeKey.valueOf("screen.loginTimeoutTask");

    private ScreenChannelAttributes() {
    }
}
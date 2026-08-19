package com.harvey.screen.tcp.session;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 设备会话：一个在线设备对应一个 Channel
 *
 * @author Harvey
 */
@Data
@AllArgsConstructor
public class ScreenSession {

    /** 设备编号 */
    private final String deviceNo;

    /** 设备连接 */
    private final Channel channel;

    /** 登录令牌 */
    private final String token;

    /** 登录时间(ms) */
    private final long loginTime;

    /** 最近心跳时间(ms) */
    private volatile long lastHeartbeatTime;

    /** 远程地址 */
    private final String remoteAddress;

    /** 设备 IP */
    private final String ip;

    /** 设备型号 */
    private final String model;
}
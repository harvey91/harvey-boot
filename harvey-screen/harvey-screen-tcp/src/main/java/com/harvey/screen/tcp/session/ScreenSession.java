package com.harvey.screen.tcp.session;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * 设备会话：一个在线设备对应一个 Channel
 *
 * @author Harvey
 */
@Data
public class ScreenSession {

    public ScreenSession(String deviceNo, Channel channel, String token, long loginTime,
                         long lastHeartbeatTime, String remoteAddress, String ip, String model) {
        this.deviceNo = deviceNo;
        this.channel = channel;
        this.token = token;
        this.loginTime = loginTime;
        this.lastHeartbeatTime = lastHeartbeatTime;
        this.remoteAddress = remoteAddress;
        this.ip = ip;
        this.model = model;
    }

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

    /** 审核状态(0待确认 1已通过 2已拒绝)，由服务端监听器填充，用于登录应答提示 */
    private volatile Integer auditStatus;
}
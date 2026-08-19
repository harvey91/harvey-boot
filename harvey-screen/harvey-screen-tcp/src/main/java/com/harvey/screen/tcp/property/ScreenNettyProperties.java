package com.harvey.screen.tcp.property;

import com.harvey.screen.api.ScreenConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 信发 Netty 服务配置
 *
 * @author Harvey
 */
@Data
@ConfigurationProperties(prefix = "harvey.screen.netty")
public class ScreenNettyProperties {

    /** 是否启用信发 TCP 服务 */
    private boolean enabled = true;

    /** 监听地址，为空时绑定所有网卡 */
    private String host;

    /** 监听端口 */
    private int port = ScreenConstants.DEFAULT_PORT;

    /** boss 线程数，<=0 时使用默认值 1 */
    private int bossThreads = 1;

    /** worker 线程数，<=0 时使用 CPU 核数 * 2 */
    private int workerThreads = 0;

    /** 连接请求队列大小 */
    private int backlog = 1024;

    /** TCP_NODELAY，禁用 Nagle 降低指令延迟 */
    private boolean tcpNoDelay = true;

    /** SO_KEEPALIVE */
    private boolean keepAlive = true;

    /** 读空闲秒数，超过则判定设备心跳超时并断开(0 表示不检测) */
    private int readerIdleSeconds = 90;

    /** 写空闲秒数(0 表示不检测) */
    private int writerIdleSeconds = 0;

    /** 读/写空闲秒数(0 表示不检测) */
    private int allIdleSeconds = 0;

    /** 登录超时秒数，连接建立后未完成登录则断开 */
    private int loginTimeoutSeconds = 30;

    /** 最大报文长度 */
    private int maxFrameLength = ScreenConstants.DEFAULT_MAX_FRAME_LENGTH;

    /** 鉴权配置 */
    private Auth auth = new Auth();

    @Data
    public static class Auth {

        /** 是否启用设备登录鉴权 */
        private boolean required = true;

        /** 登录 token 白名单，为空时允许任意非空 token(骨架阶段，后续接入 Redis/DB 校验) */
        private List<String> tokenWhitelist = new ArrayList<>();
    }
}
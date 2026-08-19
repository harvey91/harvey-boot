package com.harvey.screen.cluster;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 信发集群配置
 *
 * @author Harvey
 */
@Data
@ConfigurationProperties(prefix = "harvey.screen.cluster")
public class ScreenClusterProperties {

    /** 是否启用多节点集群(依赖 Redis) */
    private boolean enabled = true;

    /** 节点id，为空时默认取 本机IP:TCP端口(端口唯一，保证节点id唯一) */
    private String nodeId = "";

    /** 在线登记 TTL(秒)，心跳续期；节点宕机后超过该时长自动离线 */
    private long onlineTtlSeconds = 120;

    /** 跨节点指令转发的 Redis 频道 */
    private String commandChannel = "harvey:screen:command";
}
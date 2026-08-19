package com.harvey.screen.cluster;

import cn.hutool.json.JSONUtil;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.tcp.listener.ScreenSessionListener;
import com.harvey.screen.tcp.property.ScreenNettyProperties;
import com.harvey.screen.tcp.session.ScreenSession;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import com.harvey.screen.tcp.support.ScreenCommandSender;
import com.harvey.starter.redis.service.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 信发集群会话注册中心
 * <p>
 * 多节点部署时，各节点将本机在线设备登记到 Redis 的独立 Set(key 含 nodeId)，
 * 心跳续期 TTL；节点宕机后其 Set 自动过期，实现全局在线状态。
 * 指令下发优先本机会话，本机无会话时经 Redis 频道转发到设备所在节点。
 * 单机模式(集群禁用或 Redis 异常)自动回退到本机会话管理器。
 *
 * @author Harvey
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScreenClusterSessionRegistry implements ScreenSessionListener {

    private static final String ONLINE_KEY_PREFIX = "harvey:screen:node:";
    private static final String ONLINE_KEY_SUFFIX = ":online";

    /** 消息类型：指令转发 */
    private static final String TYPE_COMMAND = "COMMAND";

    /** 消息类型：会话接管(设备在新节点登录，踢掉旧节点残留会话) */
    private static final String TYPE_TAKEOVER = "TAKEOVER";

    private final ScreenSessionManager sessionManager;
    private final RedisService redisService;
    private final ScreenClusterProperties props;
    private final ScreenNettyProperties nettyProps;
    private final ScreenCommandSender commandSender;

    private String nodeId;
    private String onlineKey;

    @PostConstruct
    public void init() {
        nodeId = resolveNodeId();
        onlineKey = ONLINE_KEY_PREFIX + nodeId + ONLINE_KEY_SUFFIX;
        if (!props.isEnabled()) {
            return;
        }
        try {
            redisService.delete(onlineKey);
            log.info("信发集群节点已注册: nodeId={}, onlineKey={}, onlineTtl={}s, channel={}",
                    nodeId, onlineKey, props.getOnlineTtlSeconds(), props.getCommandChannel());
        } catch (Exception e) {
            log.error("信发集群节点注册失败, 回退单机模式: nodeId={}", nodeId, e);
        }
    }

    private String resolveNodeId() {
        if (props.getNodeId() != null && !props.getNodeId().isBlank()) {
            return props.getNodeId();
        }
        String host = "unknown";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }
        return host + ":" + nettyProps.getPort();
    }

    /** 本节点id */
    public String localNodeId() {
        return nodeId;
    }

    /* ------------------- ScreenSessionListener ------------------- */

    @Override
    public void onLogin(ScreenSession session) {
        if (!props.isEnabled()) {
            return;
        }
        try {
            redisService.sAdd(onlineKey, session.getDeviceNo());
            redisService.expire(onlineKey, props.getOnlineTtlSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("集群上线登记失败: deviceNo={}", session.getDeviceNo(), e);
        }
        publishTakeover(session.getDeviceNo());
    }

    @Override
    public void onHeartbeat(ScreenSession session) {
        if (!props.isEnabled()) {
            return;
        }
        try {
            redisService.expire(onlineKey, props.getOnlineTtlSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("集群心跳续期失败: deviceNo={}", session.getDeviceNo(), e);
        }
    }

    @Override
    public void onLogout(ScreenSession session) {
        if (!props.isEnabled()) {
            return;
        }
        try {
            redisService.sRemove(onlineKey, session.getDeviceNo());
        } catch (Exception e) {
            log.error("集群下线登记失败: deviceNo={}", session.getDeviceNo(), e);
        }
    }

    /* ------------------- 全局在线状态查询 ------------------- */

    /**
     * 全局是否在线：本机会话快路径，无则查询各节点 Redis Set
     */
    public boolean isOnlineGlobal(String deviceNo) {
        if (sessionManager.isOnline(deviceNo)) {
            return true;
        }
        if (!props.isEnabled()) {
            return false;
        }
        try {
            for (String key : redisService.keys(ONLINE_KEY_PREFIX + "*" + ONLINE_KEY_SUFFIX)) {
                if (Boolean.TRUE.equals(redisService.isMember(key, deviceNo))) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn("查询全局在线状态失败, 视为离线: deviceNo={}", deviceNo, e);
            return false;
        }
        return false;
    }

    /**
     * 设备所在节点id，本机无会话时查询各节点 Redis Set
     *
     * @return 在线返回节点id，离线返回 null
     */
    public String ownerNodeId(String deviceNo) {
        if (sessionManager.isOnline(deviceNo)) {
            return nodeId;
        }
        if (!props.isEnabled()) {
            return null;
        }
        try {
            for (String key : redisService.keys(ONLINE_KEY_PREFIX + "*" + ONLINE_KEY_SUFFIX)) {
                if (Boolean.TRUE.equals(redisService.isMember(key, deviceNo))) {
                    return key.substring(ONLINE_KEY_PREFIX.length(), key.length() - ONLINE_KEY_SUFFIX.length());
                }
            }
        } catch (Exception e) {
            log.warn("查询设备所在节点失败: deviceNo={}", deviceNo, e);
            return null;
        }
        return null;
    }

    /**
     * 全局在线设备数(各节点 Set 大小之和)
     */
    public int onlineCountGlobal() {
        if (!props.isEnabled()) {
            return sessionManager.onlineCount();
        }
        try {
            int count = 0;
            for (String key : redisService.keys(ONLINE_KEY_PREFIX + "*" + ONLINE_KEY_SUFFIX)) {
                Long size = redisService.sSize(key);
                if (size != null) {
                    count += size;
                }
            }
            return count;
        } catch (Exception e) {
            log.warn("统计全局在线设备数失败, 回退本机数量", e);
            return sessionManager.onlineCount();
        }
    }

    /* ------------------- 跨节点指令转发 ------------------- */

    /**
     * 本机无会话时，将指令转发到设备所在节点
     *
     * @return true=已发布到 Redis 频道(由目标节点负责下发)
     */
    public boolean forwardCommand(String deviceNo, long seq, int cmdCode, Map<String, Object> params) {
        String owner = ownerNodeId(deviceNo);
        if (owner == null) {
            return false;
        }
        if (owner.equals(nodeId)) {
            return false;
        }
        if (!props.isEnabled()) {
            return false;
        }
        try {
            redisService.redisTemplate.convertAndSend(props.getCommandChannel(),
                    new ScreenCommandForwardMessage(TYPE_COMMAND, deviceNo, seq, cmdCode, params, nodeId, System.currentTimeMillis()));
            log.info("指令已转发到集群节点: deviceNo={}, target={}, cmdCode={}, seq={}", deviceNo, owner, cmdCode, seq);
            return true;
        } catch (Exception e) {
            log.error("指令转发失败: deviceNo={}, target={}", deviceNo, owner, e);
            return false;
        }
    }

    /**
     * 广播会话接管：设备在本节点登录后，通知其他节点关闭该设备的残留会话，保证全局唯一会话
     */
    private void publishTakeover(String deviceNo) {
        if (!props.isEnabled()) {
            return;
        }
        try {
            redisService.redisTemplate.convertAndSend(props.getCommandChannel(),
                    new ScreenCommandForwardMessage(TYPE_TAKEOVER, deviceNo, null, null, null, nodeId, System.currentTimeMillis()));
        } catch (Exception e) {
            log.error("会话接管广播失败: deviceNo={}", deviceNo, e);
        }
    }

    /**
     * 订阅到集群消息：TAKEOVER 踢掉残留会话；COMMAND 确认设备在本节点后下发
     */
    public void handleForwardMessage(String body) {
        ScreenCommandForwardMessage msg;
        try {
            msg = JSONUtil.toBean(body, ScreenCommandForwardMessage.class);
        } catch (Exception e) {
            log.error("集群消息解析失败: body={}", body, e);
            return;
        }
        if (msg == null || msg.getDeviceNo() == null) {
            return;
        }
        if (TYPE_TAKEOVER.equals(msg.getType())) {
            handleTakeover(msg);
            return;
        }
        handleCommand(msg);
    }

    private void handleTakeover(ScreenCommandForwardMessage msg) {
        if (nodeId.equals(msg.getFromNodeId())) {
            return;
        }
        ScreenSession local = sessionManager.get(msg.getDeviceNo());
        if (local == null) {
            return;
        }
        log.warn("设备在其他节点上线, 关闭本节点残留会话: deviceNo={}, owner={}", msg.getDeviceNo(), msg.getFromNodeId());
        local.getChannel().close();
    }

    private void handleCommand(ScreenCommandForwardMessage msg) {
        if (!sessionManager.isOnline(msg.getDeviceNo())) {
            log.warn("收到转发指令但设备不在本节点, 丢弃: deviceNo={}, from={}", msg.getDeviceNo(), msg.getFromNodeId());
            return;
        }
        commandSender.send(msg.getDeviceNo(), ScreenMessage.command(msg.getSeq(), msg.getCmdCode(), msg.getParams()));
    }
}
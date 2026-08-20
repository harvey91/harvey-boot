package com.harvey.screen.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.harvey.screen.cluster.ScreenClusterProperties;
import com.harvey.screen.mapper.ScreenCommandMapper;
import com.harvey.screen.model.entity.ScreenCommand;
import com.harvey.screen.service.ScreenCommandService;
import com.harvey.starter.redis.service.RedisService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 指令超时轮询
 * <p>
 * 周期性扫描长时间处于"已发送"且无设备应答的指令，流转为"超时"。
 * 采用条件 UPDATE(仅命中 status=已发送 且 sendTime 早于截止时间)，多节点并发执行天然幂等。
 * 多节点集群(harvey.screen.cluster.enabled)下通过 Redis 分布式锁保证每轮仅一个节点执行，避免冗余扫描。
 * 自带调度线程，不依赖全局 @EnableScheduling，避免影响其他模块。
 *
 * @author Harvey
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScreenCommandTimeoutTask {

    /** 分布式锁 key(集群模式下用于保证每轮仅一个节点执行) */
    private static final String LOCK_KEY = "harvey:screen:command-timeout:lock";

    private final ScreenCommandMapper commandMapper;
    private final ScreenCommandTimeoutProperties props;
    private final RedisService redisService;
    private final ScreenClusterProperties clusterProps;

    private ScheduledExecutorService executor;
    private volatile boolean running;

    @PostConstruct
    public void start() {
        if (!props.isEnabled()) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "screen-command-timeout");
            thread.setDaemon(true);
            return thread;
        });
        running = true;
        executor.scheduleWithFixedDelay(this::sweep,
                props.getScanIntervalSeconds(), props.getScanIntervalSeconds(), TimeUnit.SECONDS);
        log.info("指令超时轮询已启动: timeout={}s, interval={}s",
                props.getTimeoutSeconds(), props.getScanIntervalSeconds());
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /**
     * 扫描一次：已发送且超过应答时限的指令置为"超时"
     */
    public void sweep() {
        if (!running || !props.isEnabled()) {
            return;
        }
        String lockValue = null;
        if (clusterProps.isEnabled()) {
            lockValue = UUID.randomUUID().toString();
            try {
                if (!redisService.setIfAbsent(LOCK_KEY, lockValue, props.getScanIntervalSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            } catch (Exception e) {
                log.error("指令超时轮询获取分布式锁失败, 本轮跳过", e);
                return;
            }
        }
        try {
            LocalDateTime cutoff = LocalDateTime.now().minusSeconds(props.getTimeoutSeconds());
            LambdaUpdateWrapper<ScreenCommand> wrapper = new LambdaUpdateWrapper<ScreenCommand>()
                    .eq(ScreenCommand::getStatus, ScreenCommandService.STATUS_SENT)
                    .lt(ScreenCommand::getSendTime, cutoff)
                    .set(ScreenCommand::getStatus, ScreenCommandService.STATUS_TIMEOUT)
                    .set(ScreenCommand::getAckTime, LocalDateTime.now())
                    .set(ScreenCommand::getAckMessage, "指令应答超时");
            int count = commandMapper.update(null, wrapper);
            if (count > 0) {
                log.info("指令超时轮询: 判定超时 {} 条", count);
            }
        } catch (Exception e) {
            log.error("指令超时轮询执行异常", e);
        } finally {
            releaseLock(lockValue);
        }
    }

    /**
     * 释放分布式锁(仅释放自己的锁, 防止误删他人锁)
     */
    private void releaseLock(String lockValue) {
        if (lockValue == null || !clusterProps.isEnabled()) {
            return;
        }
        try {
            if (lockValue.equals(redisService.get(LOCK_KEY))) {
                redisService.delete(LOCK_KEY);
            }
        } catch (Exception e) {
            log.warn("指令超时轮询释放分布式锁失败(由 TTL 兜底)", e);
        }
    }
}
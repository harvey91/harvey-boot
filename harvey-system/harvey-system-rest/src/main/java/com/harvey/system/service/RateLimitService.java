package com.harvey.system.service;

import com.harvey.common.constant.CacheConstant;
import com.harvey.starter.redis.service.RedisService;
import com.harvey.system.model.ratelimit.IpBanVO;
import com.harvey.system.model.ratelimit.RateLimitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 限流与封IP 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {

    /** 默认时间窗口(秒) */
    private static final int DEFAULT_WINDOW_SECONDS = 60;
    /** 默认窗口内最大请求次数 */
    private static final long DEFAULT_MAX_COUNT = 100;
    /** 默认超限封禁时长(秒) */
    private static final int DEFAULT_BAN_SECONDS = 600;

    private static final String FIELD_BAN_TIME = "banTimeMillis";
    private static final String FIELD_EXPIRE_TIME = "expireTimeMillis";

    private final RedisService redisService;

    /**
     * 获取限流配置，未配置时初始化默认值
     */
    public RateLimitConfig getConfig() {
        RateLimitConfig config = redisService.get(CacheConstant.RATE_LIMIT_CONFIG_KEY);
        if (config == null) {
            config = defaultConfig();
            redisService.set(CacheConstant.RATE_LIMIT_CONFIG_KEY, config);
        }
        return config;
    }

    /**
     * 修改限流配置
     */
    public void updateConfig(RateLimitConfig config) {
        if (config.getEnabled() == null) {
            config.setEnabled(true);
        }
        redisService.set(CacheConstant.RATE_LIMIT_CONFIG_KEY, config);
    }

    /**
     * 判断IP是否被封禁
     */
    public boolean isBanned(String ip) {
        return Boolean.TRUE.equals(redisService.hasKey(CacheConstant.RATE_LIMIT_BAN_KEY + ip));
    }

    /**
     * 封禁IP
     *
     * @param ip         IP地址
     * @param banSeconds 封禁时长(秒)，为空时使用当前配置
     */
    public void ban(String ip, Integer banSeconds) {
        long banSecondsLong = banSeconds == null ? getConfig().getBanSeconds() : banSeconds;
        long now = System.currentTimeMillis();
        Map<String, Long> banInfo = new HashMap<>();
        banInfo.put(FIELD_BAN_TIME, now);
        banInfo.put(FIELD_EXPIRE_TIME, now + banSecondsLong * 1000);
        redisService.setEx(CacheConstant.RATE_LIMIT_BAN_KEY + ip, banInfo, banSecondsLong, TimeUnit.SECONDS);
    }

    /**
     * 解封IP
     */
    public void unban(String ip) {
        redisService.delete(CacheConstant.RATE_LIMIT_BAN_KEY + ip);
    }

    /**
     * 封禁IP列表
     */
    public List<IpBanVO> listBlacklist() {
        Set<String> keys = redisService.keys(CacheConstant.RATE_LIMIT_BAN_KEY + "*");
        List<IpBanVO> list = new ArrayList<>();
        if (keys == null || keys.isEmpty()) {
            return list;
        }
        long now = System.currentTimeMillis();
        for (String key : keys) {
            Object value = redisService.get(key);
            if (!(value instanceof Map<?, ?> map)) {
                continue;
            }
            Object expireObj = map.get(FIELD_EXPIRE_TIME);
            if (!(expireObj instanceof Number expireNumber)) {
                continue;
            }
            long expireTimeMillis = expireNumber.longValue();
            long remaining = expireTimeMillis - now;
            // 已过期，清理缓存
            if (remaining <= 0) {
                redisService.delete(key);
                continue;
            }
            long banTimeMillis = now;
            if (map.get(FIELD_BAN_TIME) instanceof Number banNumber) {
                banTimeMillis = banNumber.longValue();
            }
            IpBanVO banInfo = new IpBanVO();
            banInfo.setIp(key.substring(CacheConstant.RATE_LIMIT_BAN_KEY.length()));
            banInfo.setRemainingSeconds(remaining / 1000);
            banInfo.setBanTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(banTimeMillis), ZoneId.systemDefault()));
            banInfo.setExpireTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(expireTimeMillis), ZoneId.systemDefault()));
            list.add(banInfo);
        }
        list.sort(Comparator.comparing(IpBanVO::getExpireTime).reversed());
        return list;
    }

    /**
     * 默认限流配置
     */
    private RateLimitConfig defaultConfig() {
        RateLimitConfig config = new RateLimitConfig();
        config.setEnabled(true);
        config.setWindowSeconds(DEFAULT_WINDOW_SECONDS);
        config.setMaxCount((int) DEFAULT_MAX_COUNT);
        config.setBanSeconds(DEFAULT_BAN_SECONDS);
        return config;
    }
}

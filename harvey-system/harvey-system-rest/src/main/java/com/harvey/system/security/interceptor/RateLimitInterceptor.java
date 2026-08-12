package com.harvey.system.security.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harvey.common.constant.CacheConstant;
import com.harvey.common.result.RespResult;
import com.harvey.common.utils.ip.IpUtils;
import com.harvey.starter.redis.service.RedisService;
import com.harvey.system.model.ratelimit.RateLimitConfig;
import com.harvey.system.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 限流与封IP拦截器
 * <p>
 * 按IP统计窗口内请求次数，超过阈值自动封禁IP；被封禁的IP直接拒绝请求
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        RateLimitConfig config = rateLimitService.getConfig();
        if (config == null || !Boolean.TRUE.equals(config.getEnabled())) {
            return true;
        }
        String ip = IpUtils.getIpAddr(request);
        // 已被封禁的IP直接拒绝
        if (rateLimitService.isBanned(ip)) {
            log.warn("拦截被封禁IP请求，ip={}, uri={}", ip, request.getRequestURI());
            reject(response, "请求过于频繁，IP已被临时封禁，请稍后再试");
            return false;
        }
        // 窗口内计数器自增，首次请求时设置窗口过期时间
        String counterKey = CacheConstant.RATE_LIMIT_COUNTER_KEY + ip;
        Long count = redisService.incrBy(counterKey, 1);
        if (count != null && count == 1) {
            redisService.expire(counterKey, config.getWindowSeconds(), TimeUnit.SECONDS);
        }
        // 超过阈值自动封禁IP
        if (count != null && count > config.getMaxCount()) {
            log.warn("IP请求超限自动封禁，ip={}, count={}, max={}, uri={}", ip, count, config.getMaxCount(), request.getRequestURI());
            rateLimitService.ban(ip, config.getBanSeconds());
            reject(response, "请求过于频繁，IP已被临时封禁，请稍后再试");
            return false;
        }
        return true;
    }

    /**
     * 拒绝请求并返回 JSON
     */
    private void reject(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(RespResult.fail(HttpStatus.TOO_MANY_REQUESTS.value(), msg)));
    }
}

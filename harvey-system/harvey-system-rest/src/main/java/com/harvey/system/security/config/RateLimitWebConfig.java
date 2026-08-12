package com.harvey.system.security.config;

import com.harvey.system.security.interceptor.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 限流与封IP拦截器配置
 * <p>
 * order 高于 sa-token 拦截器，限流检查先于登录鉴权执行，可保护登录等匿名接口
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Order(0)
@Configuration
@RequiredArgsConstructor
public class RateLimitWebConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 限流管理接口(避免管理员误封自己被锁死)
                        "/system/rateLimit/**",
                        // 静态资源
                        "/*.html",
                        "/*/*.html",
                        "/*/*.css",
                        "/*/*.js",
                        // websocket
                        "/webSocket/**",
                        // swagger 文档
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        // 文件
                        "/avatar/**",
                        "/file/**",
                        "/storage/fetch/**",
                        // druid
                        "/druid/**",
                        // 健康检查
                        "/actuator/**"
                );
    }
}

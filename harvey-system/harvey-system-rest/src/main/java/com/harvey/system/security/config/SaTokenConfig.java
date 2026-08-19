package com.harvey.system.security.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa-token 配置
 *
 * @author Harvey
 * @date 2024-11-04 10:28
 **/
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册 sa-token 拦截器，所有请求都需要登录，注解鉴权（@SaCheckPermission 等）由 SaInterceptor 自动处理
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 异步派发(SSE/流式响应)时, 当前线程无 SA-Token 上下文, 登录校验已在首轮请求完成, 直接放行
                    if (!SaManager.getSaTokenContext().isValid()) {
                        return;
                    }
                    // 放行 OPTIONS 预检请求
                    SaRouter.match(SaHttpMethod.OPTIONS).free(r -> {});
                    // 其余请求都需要登录
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                }))
                .addPathPatterns("/**")
                // 匿名访问 url
                .excludePathPatterns(
                        "/authorize/login",
                        "/authorize/captcha",
                        // 静态资源
                        "/*.html",
                        "/*/*.html",
                        "/*/*.css",
                        "/*/*.js",
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
                        // 阿里巴巴 druid
                        "/druid/**"
                );
    }

    /**
     * 注入密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }
}

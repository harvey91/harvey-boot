package com.harvey.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Harvey
 * @date 2024-11-04 10:28
 **/
@Configuration
@EnableWebSecurity // 启动spring security
@EnableMethodSecurity // 启动全局函数权限
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final ApplicationContext applicationContext;
    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 原先的方法authorizeRequests变为authorizeHttpRequests、方法antMatchers变为requestMatchers
     * 返回SecurityFilterChain
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 所有请求都需要认证
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                // 防止iframe 造成跨域
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 前后端分离，不需要创建session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 异常授权
//                .exceptionHandling(configurer -> configurer.authenticationEntryPoint())
                // 禁用CSRF，防跨站请求伪造
                .csrf(AbstractHttpConfigurer::disable)
                // 前置过滤器
                .addFilterBefore(new JwtTokenFilter(jwtProperties, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                ;
        return httpSecurity.build();
    }

    /**
     * 允许匿名访问url配置
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 搜寻匿名标记 url： @AnonymousAccess
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获取匿名标记
//        Map<String, Set<String>> anonymousUrls = getAnonymousUrl(handlerMethodMap);
        return web -> web.ignoring()
                // 静态资源等等
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/*.html"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/*/*.html"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/*/*.css"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/*/*.js"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/webSocket/**"))
                // swagger 文档
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-resources/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/*/api-docs"))
                // 文件
                .requestMatchers(AntPathRequestMatcher.antMatcher("/avatar/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/file/**"))
                // 阿里巴巴 druid
                .requestMatchers(AntPathRequestMatcher.antMatcher("/druid/**"))
                // 允许所有OPTIONS请求
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/authorize/**"));
                // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
                // GET
//                .requestMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0]))
//                // POST
//                .requestMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0]))
//                // PUT
//                .requestMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0]))
//                // PATCH
//                .requestMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0]))
//                // DELETE
//                .requestMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0]))
//                // 所有类型的接口都放行
//                .requestMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0]));
    }

    /*private Map<String, Set<String>> getAnonymousUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, Set<String>> anonymousUrls = new HashMap<>(8);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.size() == 0 ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                switch (Objects.requireNonNull(request)) {
                    case GET:
                        get.addAll(infoEntry.getKey().getPathPatternsCondition().getDirectPaths());
                        break;
                    case POST:
                        post.addAll(infoEntry.getKey().getPathPatternsCondition().getDirectPaths());
                        break;
                    case PUT:
                        put.addAll(infoEntry.getKey().getPathPatternsCondition().getDirectPaths());
                        break;
                    case PATCH:
                        patch.addAll(infoEntry.getKey().getPathPatternsCondition().getDirectPaths());
                        break;
                    case DELETE:
                        delete.addAll(infoEntry.getKey().getPathPatternsCondition().getDirectPaths());
                        break;
                    default:
                        all.addAll(infoEntry.getKey().getPathPatternsCondition().getDirectPaths());
                        break;
                }
            }
        }
        anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(), all);
        return anonymousUrls;
    }*/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
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

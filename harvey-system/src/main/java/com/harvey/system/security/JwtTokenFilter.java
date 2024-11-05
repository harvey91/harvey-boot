package com.harvey.system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * @author Harvey
 * @date 2024-11-05 18:43
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = jwtTokenProvider.getToken(request);
        log.debug("url: {}", request.getRequestURI());
        log.debug("token: {}", token);
        if (ObjectUtils.isEmpty(token)) {
            log.debug("非法Token：{}", token);
            // TODO 返回错误码
            return;
        }

        // TODO token是否过期
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // 把token认证设置到spring上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

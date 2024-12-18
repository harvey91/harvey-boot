package com.harvey.system.security.filter;

import com.alibaba.fastjson2.JSON;
import com.harvey.common.base.RespResult;
import com.harvey.common.enums.ErrorCodeEnum;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.service.JwtTokenService;
import com.harvey.common.utils.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * @author Harvey
 * @date 2024-11-05 18:43
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends GenericFilterBean {
    private final JwtTokenService jwtTokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = jwtTokenService.getToken(request);
        log.debug("url: {}, token: {}", request.getRequestURI(), token);
        if (ObjectUtils.isEmpty(token)) {
            log.debug("非法Token：{}", token);
            ServletUtils.renderString(response, JSON.toJSONString(RespResult.fail(ErrorCodeEnum.NOT_LOGIN)));
            return;
        }

        LoginUserVO loginUserVO = jwtTokenService.getLoginUser(token);
        if (ObjectUtils.isEmpty(loginUserVO)) {
            ServletUtils.renderString(response, JSON.toJSONString(RespResult.fail(ErrorCodeEnum.NOT_LOGIN)));
            return;
        }
        jwtTokenService.verifyToken(loginUserVO);
        Authentication authentication = jwtTokenService.getAuthentication(loginUserVO);
        // 把token认证设置到security上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

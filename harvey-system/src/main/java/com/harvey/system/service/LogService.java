package com.harvey.system.service;

import com.harvey.system.model.dto.LogLoginDto;
import com.harvey.system.model.dto.LogOpDto;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.utils.ServletUtils;
import com.harvey.system.utils.ip.AddressUtils;
import com.harvey.system.utils.ip.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Harvey
 * @date 2024-11-25 13:26
 **/
@Service
@RequiredArgsConstructor
public class LogService {
    private final LogOpService logOpService;
    private final LogLoginService logLoginService;

    /**
     * 保存登陆登出日志
     * @param userId
     * @param username
     * @param result
     * @param remark
     */
    public void saveLoginLog(Long userId, String username, Integer result, String remark) {
        HttpServletRequest request = ServletUtils.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        LogLoginDto logLoginDto = LogLoginDto.builder()
                .userId(userId)
                .username(username)
                .ip(ip)
                .location(address)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .result(result)
                .remark(remark).build();
        logLoginService.saveLogLogin(logLoginDto);
    }

    /**
     * 保存操作日志
     * @param module
     * @param method
     * @param params
     * @param duration
     * @param result
     * @param detail
     */
    public void saveLogOp(String module, String method, String params, Long duration, Integer result, String detail) {
        HttpServletRequest request = ServletUtils.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        String requestUri = request.getRequestURI();
        Optional<LoginUserVO> loginUserVO = SecurityUtil.getLoginUserVO();
        LogOpDto logOpDto = LogOpDto.builder()
                .userId(loginUserVO.map(LoginUserVO::getUserId).orElse(0L))
                .operator(loginUserVO.map(LoginUserVO::getUsername).orElse(""))
                .module(module)
                .requestUri(requestUri)
                .method(method)
                .params(params)
                .ip(ip)
                .location(address)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .duration(duration)
                .result(result)
                .detail(detail)
                .build();
        logOpService.saveLogOp(logOpDto);
    }
}

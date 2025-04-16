package com.harvey.system.security.service;

import com.harvey.common.constant.CacheConstant;
import com.harvey.common.constant.Constant;
import com.harvey.common.utils.ServletUtils;
import com.harvey.common.utils.ip.AddressUtils;
import com.harvey.common.utils.ip.IpUtils;
import com.harvey.starter.redis.service.RedisService;
import com.harvey.system.model.dto.OnlineUserDto;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.service.OnlineUserService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author Harvey
 * @date 2024-11-13 22:21
 **/
@Service
@RequiredArgsConstructor
public class OnlineUserCacheService {
    private final OnlineUserService onlineUserService;
    private final RedisService redisService;

    public void save(LoginUserVO loginUserVO, Integer expireTime, boolean refresh) {
        loginUserVO.setLoginTime(System.currentTimeMillis());
        loginUserVO.setExpireTime(loginUserVO.getLoginTime() + expireTime * Constant.MILLIS_MINUTE);
        setUserAgent(loginUserVO);
        // 缓存
        String userKey = getLoginUserKey(loginUserVO.getUuid());
        redisService.setEx(userKey, loginUserVO, expireTime, TimeUnit.MINUTES);
        OnlineUserDto onlineUserDto = getOnlineUserDto(loginUserVO);
        // 入库
        if (refresh) {
            onlineUserService.updateExpireTime(onlineUserDto);
        } else {
            onlineUserService.saveByLoginUser(onlineUserDto);
        }
    }


    public void delete(String uuid) {
        onlineUserService.offline(uuid);
        redisService.delete(getLoginUserKey(uuid));
    }

    public LoginUserVO getLoginUser(String uuid) {
        return redisService.get(getLoginUserKey(uuid));
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUserVO loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIp(ip);
        loginUser.setLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    public String getLoginUserKey(String uuid) {
        return CacheConstant.LOGIN_TOKEN_KEY + uuid;
    }

    public OnlineUserDto getOnlineUserDto(LoginUserVO loginUserVO) {
        OnlineUserDto onlineUserDto = new OnlineUserDto();
        onlineUserDto.setUuid(loginUserVO.getUuid());
        onlineUserDto.setUserId(loginUserVO.getUserId());
        onlineUserDto.setUsername(loginUserVO.getUsername());
        onlineUserDto.setDeptName("");
        onlineUserDto.setIp(loginUserVO.getIp());
        onlineUserDto.setLocation(loginUserVO.getLocation());
        onlineUserDto.setBrowser(loginUserVO.getBrowser());
        onlineUserDto.setOs(loginUserVO.getOs());
        LocalDateTime loginTime = Instant.ofEpochMilli(loginUserVO.getLoginTime()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        LocalDateTime expireTime = Instant.ofEpochMilli(loginUserVO.getExpireTime()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        onlineUserDto.setCreateTime(loginTime);
        onlineUserDto.setExpireTime(expireTime);
        onlineUserDto.setStatus(Constant.ONLINE_STATUS);
        return onlineUserDto;
    }
}

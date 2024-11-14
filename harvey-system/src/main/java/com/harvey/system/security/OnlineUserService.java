package com.harvey.system.security;

import com.harvey.system.constant.CacheConstant;
import com.harvey.system.constant.Constant;
import com.harvey.system.redis.RedisCache;
import com.harvey.system.service.ISysOnlineUserService;
import com.harvey.system.utils.ServletUtils;
import com.harvey.system.utils.ip.AddressUtils;
import com.harvey.system.utils.ip.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Harvey
 * @date 2024-11-13 22:21
 **/
@Service
@RequiredArgsConstructor
public class OnlineUserService {
    private final ISysOnlineUserService sysOnlineUserService;
    private final RedisCache redisCache;

    public List<LoginUserVO> list() {
//        redisCache.
        return null;
    }

    public void save(LoginUserVO loginUserVO, Integer expireTime, boolean refresh) {
        loginUserVO.setLoginTime(System.currentTimeMillis());
        loginUserVO.setExpireTime(loginUserVO.getLoginTime() + expireTime * Constant.MILLIS_MINUTE);
        setUserAgent(loginUserVO);
        // 缓存
        String userKey = getLoginUserKey(loginUserVO.getUuid());
        redisCache.setCacheObject(userKey, loginUserVO, expireTime, TimeUnit.MINUTES);
        // 入库
        if (refresh) {
            sysOnlineUserService.updateExpireTime(loginUserVO);
        } else {
            sysOnlineUserService.saveByLoginUser(loginUserVO);
        }
    }

    public void delete(String uuid) {
        sysOnlineUserService.offline(uuid);
        redisCache.deleteObject(getLoginUserKey(uuid));
    }

    public LoginUserVO getLoginUser(String uuid) {
        return redisCache.getCacheObject(getLoginUserKey(uuid));
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
}

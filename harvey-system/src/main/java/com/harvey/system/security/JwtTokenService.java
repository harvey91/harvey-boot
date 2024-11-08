package com.harvey.system.security;

import cn.hutool.core.util.IdUtil;
import com.harvey.system.constant.CacheConstant;
import com.harvey.system.constant.Constant;
import com.harvey.system.redis.RedisCache;
import com.harvey.system.utils.ServletUtils;
import com.harvey.system.utils.ip.AddressUtils;
import com.harvey.system.utils.ip.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * @author Harvey
 * @date 2024-11-05 14:41
 **/
@Component
@RequiredArgsConstructor
public class JwtTokenService implements InitializingBean {
    private final JwtProperties jwtProperties;
    private final RedisCache redisCache;
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    /**
     * 在Bean的所有属性被Spring容器设置之后自动被调用
     * jwt签名和验证
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getBase64Secret());
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parser().verifyWith(key).build();
        jwtBuilder = Jwts.builder().signWith(key);
    }

    public String createToken(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();// 缓存登陆用户信息
        String uuid = IdUtil.fastSimpleUUID();
        loginUser.setUuid(uuid);
        setUserAgent(loginUser);
        refreshToken(loginUser);
        return jwtBuilder
                .id(uuid)
                .claim("userId", loginUser.getUserId())
                .subject(loginUser.getUsername())
                .compact();
    }

    public Claims parserToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= Constant.MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + jwtProperties.getExpireTime() * Constant.MILLIS_MINUTE);
        String userKey = getLoginUserKey(loginUser.getUserId());
        redisCache.setCacheObject(userKey, loginUser, jwtProperties.getExpireTime(), TimeUnit.MINUTES);
    }

    public Authentication getAuthentication(LoginUser loginUser) {
        return new UsernamePasswordAuthenticationToken(loginUser, "", loginUser.getAuthorities());
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestHeader != null && requestHeader.startsWith(jwtProperties.getTokenStartWith())) {
            return requestHeader.replace(jwtProperties.getTokenStartWith(), "").strip();
        }
        return null;
    }

    public LoginUser getLoginUser(String token) {
        Claims claims = parserToken(token);
        Long userId = claims.get("userId", Long.class);
        return redisCache.getCacheObject(getLoginUserKey(userId));
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpAddress(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    public String getLoginUserKey(Long userId) {
        return CacheConstant.LOGIN_TOKEN_KEY + userId;
    }
}

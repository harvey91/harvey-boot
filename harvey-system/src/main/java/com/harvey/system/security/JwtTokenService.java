package com.harvey.system.security;

import cn.hutool.core.util.IdUtil;
import com.harvey.system.constant.Constant;
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
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


/**
 * @author Harvey
 * @date 2024-11-05 14:41
 **/
@Component
@RequiredArgsConstructor
public class JwtTokenService implements InitializingBean {
    private final JwtProperties jwtProperties;
    private final OnlineUserService onlineUserService;
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

    /**
     * 生成token
     * @param authentication
     * @return
     */
    public String createToken(Authentication authentication) {
        LoginUserVO loginUser = (LoginUserVO) authentication.getPrincipal();// 缓存登陆用户信息
        String uuid = IdUtil.fastSimpleUUID();
        loginUser.setUuid(uuid);
        onlineUserService.save(loginUser, jwtProperties.getExpireTime(), false);
        return jwtBuilder
                .id(uuid)
                .claim("uuid", uuid)
                .subject(loginUser.getUsername())
                .compact();
    }

    /**
     * token解析Claims
     * @param token
     * @return
     */
    public Claims parserToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUserVO loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= Constant.MILLIS_MINUTE_TEN) {
            onlineUserService.save(loginUser, jwtProperties.getExpireTime(), true);
        }
    }

    /**
     * loginUser生成Authentication
     * @param loginUser
     * @return
     */
    public Authentication getAuthentication(LoginUserVO loginUser) {
        return new UsernamePasswordAuthenticationToken(loginUser, "", loginUser.getAuthorities());
    }

    /**
     * 获取request请求header中的token
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestHeader != null && requestHeader.startsWith(jwtProperties.getTokenStartWith())) {
            return requestHeader.replace(jwtProperties.getTokenStartWith(), "").strip();
        }
        return null;
    }

    /**
     * 根据token解析后获取缓存用户
     * @param token
     * @return
     */
    public LoginUserVO getLoginUser(String token) {
        Claims claims = parserToken(token);
        String uuid = claims.get("uuid", String.class);
        return onlineUserService.getLoginUser(uuid);
    }
}

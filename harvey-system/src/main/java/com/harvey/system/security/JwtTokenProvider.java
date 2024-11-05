package com.harvey.system.security;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.*;
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
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * @author Harvey
 * @date 2024-11-05 14:41
 **/
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {
    private final JwtProperties jwtProperties;
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

    public String buildToken(Authentication authentication) {
        return jwtBuilder
                .id(IdUtil.fastSimpleUUID())
                .claim("user", authentication.getName())
                .subject(authentication.getName())
                .compact();
    }

    public Claims parserToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parserToken(token);
        String subject = claims.getSubject();
        UserDetails userDetails = User.builder().username(subject).password("").build();
        return new UsernamePasswordAuthenticationToken(userDetails, token, new ArrayList<>());
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestHeader != null && requestHeader.startsWith(jwtProperties.getTokenStartWith())) {
            return requestHeader.replace(jwtProperties.getTokenStartWith(), "").strip();
        }
        return null;
    }
}

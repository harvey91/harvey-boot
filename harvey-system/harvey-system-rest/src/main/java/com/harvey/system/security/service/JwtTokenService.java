package com.harvey.system.security.service;

import cn.hutool.core.util.IdUtil;
import com.harvey.common.constant.Constant;
import com.harvey.common.enums.LoginResultEnum;
import com.harvey.system.security.JwtProperties;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.service.LogService;
import com.harvey.system.service.MenuService;
import com.harvey.system.service.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;


/**
 * @author Harvey
 * @date 2024-11-05 14:41
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenService implements InitializingBean {
    private final JwtProperties jwtProperties;
    private final OnlineUserCacheService onlineUserCacheService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final LogService logService;
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
        LoginUserVO loginUserVO = (LoginUserVO) authentication.getPrincipal();// 缓存登陆用户信息
        // 登陆时把当前用户的数据权限对应的deptId集合查出来，后续所有含有deptId的表格查询都通过deptId过滤
        List<Long> deptIds = roleService.getDeptIds(loginUserVO.getUserId(), loginUserVO.getDeptId());
        // 用户菜单权限列表
        List<String> permissions = menuService.getPermissionByUserId(loginUserVO.getUserId());
        log.debug("permissions list: {}", permissions);
        // 用户角色列表
        List<String> roleCodeList = roleService.getRoleCodeList(loginUserVO.getUserId());
        List<SimpleGrantedAuthority> authorities = roleCodeList.stream().map(SimpleGrantedAuthority::new).toList();
        loginUserVO.setDataScopes(deptIds);
        loginUserVO.setPermissions(permissions);
        loginUserVO.setAuthorities(authorities);
        loginUserVO.setUuid(IdUtil.fastSimpleUUID());
        // 缓存在线用户
        onlineUserCacheService.save(loginUserVO, jwtProperties.getExpireTime(), false);
        // 保存登陆日志
        logService.saveLoginLog(loginUserVO.getUserId(), loginUserVO.getUsername(), LoginResultEnum.LOGIN_SUCCESS.getValue(), "");
        return jwtBuilder
                .id(loginUserVO.getUuid())
                .claim("userId", loginUserVO.getUserId())
                .subject(loginUserVO.getUsername())
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
            onlineUserCacheService.save(loginUser, jwtProperties.getExpireTime(), true);
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
        return onlineUserCacheService.getLoginUser(claims.getId());
    }
}

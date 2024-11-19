package com.harvey.system.controller.auth;

import com.harvey.system.base.RespResult;
import com.harvey.system.model.dto.LoginDto;
import com.harvey.system.security.JwtTokenService;
import com.harvey.system.security.OnlineUserCacheService;
import com.harvey.system.security.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户授权
 * @author Harvey
 * @date 2024-10-30 10:35
 **/
@Tag(name = "用户认证Controller")
@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final OnlineUserCacheService onlineUserCacheService;

    @PostMapping("/login")
    public RespResult<Object> login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenService.createToken(authentication);
        Map<String, String> data = new HashMap<>();
        data.put("accessToken", "Bearer " + token);
        return RespResult.success(data);
    }

    @DeleteMapping("/logout")
    public RespResult<Object> logout() {
        onlineUserCacheService.delete(SecurityUtil.getUuid());
        return RespResult.success();
    }

    @GetMapping("/captcha")
    public RespResult<Object> captcha() {
        // TODO captchaKey用于redis key

        return RespResult.success();
    }
}

package com.harvey.system.controller.auth;

import com.harvey.system.base.RespResult;
import com.harvey.system.domain.dto.LoginDto;
import com.harvey.system.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harvey
 * @date 2024-10-30 10:35
 **/
@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

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
        // TODO 认证框架注销用户，删除缓存token
        return RespResult.success();
    }

    @GetMapping("/captcha")
    public RespResult<Object> captcha() {
        // TODO captchaKey用于redis key

        return RespResult.success();
    }
}

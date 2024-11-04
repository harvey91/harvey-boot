package com.harvey.system.controller.auth;

import com.harvey.system.base.RespResult;
import com.harvey.system.domain.param.LoginParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public RespResult<Object> login(LoginParam loginParam) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword());
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        Object principal = authentication.getPrincipal();

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImRlcHRJZCI6MSwiZGF0YVNjb3BlIjoxLCJ1c2VySWQiOjIsImlhdCI6MTcyODE5MzA1MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJhZDg3NzlhZDZlYWY0OWY3OTE4M2ZmYmI5OWM4MjExMSJ9.58YHwL3sNNC22jyAmOZeSm-7MITzfHb_epBIz7LvWeA");
        data.put("tokenType", "Bearer");

        // TODO 使用认证框架认证用户，生成token，缓存token
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

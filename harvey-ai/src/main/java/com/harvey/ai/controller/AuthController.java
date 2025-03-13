package com.harvey.ai.controller;

import cn.hutool.core.lang.UUID;
import com.harvey.ai.domain.LoginDto;
import com.harvey.common.result.RespResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harvey
 * @date 2025-03-06 12:45
 **/
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public RespResult<Object> login(@RequestBody @Validated LoginDto dto) {
        if (!"harvey".equals(dto.getUsername()) || !"harvey".equals(dto.getPassword())) {
            return RespResult.fail("用户名或密码错误");
        }
        Map<String, String> data = new HashMap<>();
        data.put("accessToken", "Bearer " + UUID.fastUUID());
        return RespResult.success(data);
    }
}

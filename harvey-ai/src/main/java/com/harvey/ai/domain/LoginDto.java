package com.harvey.ai.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Harvey
 * @date 2025-03-06 20:53
 **/
@Data
public class LoginDto {
    @NotBlank(message = "用户名或密码错误")
    private String username;

    @NotBlank(message = "用户名或密码错误")
    private String password;
}

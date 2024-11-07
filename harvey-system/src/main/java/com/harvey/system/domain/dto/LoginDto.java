package com.harvey.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-10-30 10:43
 **/
@Data
public class LoginDto {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String captchaKey;

    private String captchaCode;
}

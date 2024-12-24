package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-10-30 10:43
 **/
@Data
@Schema(title = "LoginDto", description = "登录参数")
public class LoginDto {

    @Schema(title = "username", description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(title = "password", description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(title = "captchaKey", description = "验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String captchaKey;

    @Schema(title = "captchaCode", description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;
}

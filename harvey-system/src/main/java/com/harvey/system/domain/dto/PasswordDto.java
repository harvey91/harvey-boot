package com.harvey.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-11-04 23:16
 **/
@Data
public class PasswordDto {

    @NotNull(message = "用户id不能为空")
    @Schema(title = "id", description = "用户id")
    public Long id;

    @NotBlank(message = "密码不能为空")
    @Schema(title = "password", description = "密码")
    public String password;
}

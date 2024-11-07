package com.harvey.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("用户id")
    public Long id;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    public String password;
}

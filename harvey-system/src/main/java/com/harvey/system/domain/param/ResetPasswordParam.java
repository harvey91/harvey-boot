package com.harvey.system.domain.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-11-04 23:16
 **/
@Data
public class ResetPasswordParam {

    @NotNull(message = "用户id不能为空")
    public Long id;

    @NotBlank(message = "密码不能为空")
    public String password;
}

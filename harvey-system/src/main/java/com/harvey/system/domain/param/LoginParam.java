package com.harvey.system.domain.param;

import lombok.Data;

/**
 * @author Harvey
 * @date 2024-10-30 10:43
 **/
@Data
public class LoginParam {

    private String username;

    private String password;

    private String captchaKey;

    private String captchaCode;
}

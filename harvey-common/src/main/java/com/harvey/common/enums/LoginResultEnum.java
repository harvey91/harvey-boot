package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登陆结果 枚举类
 * @author Harvey
 * @date 2024-11-20 18:07
 **/
@Getter
@AllArgsConstructor
public enum LoginResultEnum {

    LOGIN_SUCCESS(1, "登陆成功"),
    LOGOUT_SUCCESS(2, "登出成功"),
    LOGIN_FAILED(3, "登陆失败"),
    ;

    private final int value;

    private final String label;

    public static LoginResultEnum get(int value) {
        for (LoginResultEnum enum1 : LoginResultEnum.values()) {
            if (enum1.getValue() == value) {
                return enum1;
            }
        }
        return null;
    }
}

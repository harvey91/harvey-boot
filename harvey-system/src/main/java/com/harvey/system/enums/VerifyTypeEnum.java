package com.harvey.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证类型 枚举类
 * @author Harvey
 * @date 2024-12-11 22:35
 **/
@Getter
@AllArgsConstructor
public enum VerifyTypeEnum {

    LOGIN(1, "登陆"),
    BIND(2, "绑定"),
    ;

    private final int value;
    private final String label;
}

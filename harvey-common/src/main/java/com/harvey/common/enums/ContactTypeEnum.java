package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 联系类型 枚举类
 * @author Harvey
 * @date 2024-12-11 22:35
 **/
@Getter
@AllArgsConstructor
public enum ContactTypeEnum {

    PHONE(1, "手机"),
    EMAIL(2, "邮箱"),
    ;

    private final int value;
    private final String label;
}

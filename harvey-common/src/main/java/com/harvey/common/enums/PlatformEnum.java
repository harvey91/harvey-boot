package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统平台 枚举类
 * @author Harvey
 * @date 2024-11-20 18:07
 **/
@Getter
@AllArgsConstructor
public enum PlatformEnum {

    SYSTEM(1, "后台管理"),
    MALL(2, "商城"),
    ;

    private final int value;
    private final String label;
}

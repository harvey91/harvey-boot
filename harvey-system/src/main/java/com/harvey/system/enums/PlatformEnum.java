package com.harvey.system.enums;

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

    SYSTEM("后台管理"),
    MALL( "商城"),
    ;

    private final String label;
}

package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Harvey
 * @date 2024-11-06 21:54
 **/
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    ERROR(-1, "系统错误"),
    FAIL(0, "失败"),
    SUCCESS(1, "成功"),
    NOT_LOGIN(999, "登录已过期"),
    PARAM_ERROR(1001, "参数错误")
    ;

    private Integer code;

    private String msg;

}

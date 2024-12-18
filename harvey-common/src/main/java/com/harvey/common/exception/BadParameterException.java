package com.harvey.common.exception;

import lombok.Getter;

/**
 * 参数错误异常
 *
 * @author Harvey
 */
@Getter
public final class BadParameterException extends RuntimeException {

    public BadParameterException() {
        super("参数错误");
    }

    public BadParameterException(String msg) {
        super(msg);
    }
}
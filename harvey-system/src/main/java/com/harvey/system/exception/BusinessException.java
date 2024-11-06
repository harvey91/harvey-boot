package com.harvey.system.exception;

import com.harvey.system.enums.ErrorCodeEnum;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author Harvey
 */
@Getter
public final class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    public BusinessException(String msg) {
        super(msg);
        this.code = ErrorCodeEnum.ERROR.getCode();
    }

    public BusinessException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum resultMsgEnum) {
        super(resultMsgEnum.getMsg());
        this.code = resultMsgEnum.getCode();
    }
}
package com.harvey.common.exception;

import com.harvey.common.enums.ErrorCodeEnum;
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

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum resultMsgEnum) {
        super(resultMsgEnum.getMsg());
        this.code = resultMsgEnum.getCode();
    }
}
package com.harvey.system.base;

import com.harvey.system.enums.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Harvey
 * @date 2024-10-29 10:06
 **/
@Data
public class RespResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int SUCCESS = 1;
    private static final int FAIL = 0;

    private int code;

    private String msg;

    private T data;

    public RespResult(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> RespResult<T> of(T data, int code, String msg) {
        return new RespResult<T>(data, code, msg);
    }

    public static <T> RespResult<T> success() {
        return of(null, SUCCESS, "成功");
    }
    public static <T> RespResult<T> success(T data) {
        return of(data, SUCCESS, "成功");
    }

    public static <T> RespResult<T> fail() {
        return of(null, FAIL, "失败");
    }

    public static <T> RespResult<T> fail(T data) {
        return of(data, FAIL, "失败");
    }

    public static <T> RespResult<T> fail(ErrorCodeEnum errorCodeEnum) {
        return of(null, errorCodeEnum.getCode(), errorCodeEnum.getMsg());
    }

    public static <T> RespResult<T> fail(String msg) {
        return of(null, FAIL, msg);
    }

    public static <T> RespResult<T> fail(int code, String msg) {
        return of(null, code, msg);
    }

    public static <T> RespResult<T> error() {
        return of(null, ErrorCodeEnum.ERROR.getCode(), ErrorCodeEnum.ERROR.getMsg());
    }

    public static <T> RespResult<T> error(String msg) {
        return of(null, ErrorCodeEnum.ERROR.getCode(), msg);
    }

}

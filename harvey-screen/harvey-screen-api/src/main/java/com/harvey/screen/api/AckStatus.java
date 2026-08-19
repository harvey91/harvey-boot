package com.harvey.screen.api;

/**
 * 指令应答状态
 *
 * @author Harvey
 */
public enum AckStatus {

    /** 成功 */
    OK(0),
    /** 设备忙 */
    DEVICE_BUSY(1),
    /** 未知指令 */
    UNKNOWN_CMD(2),
    /** 参数非法 */
    INVALID_PARAM(3),
    /** 设备内部错误 */
    INTERNAL_ERROR(4),
    /** 未认证 */
    UNAUTHORIZED(5);

    private final int code;

    AckStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AckStatus of(int code) {
        for (AckStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知应答状态: " + code);
    }
}
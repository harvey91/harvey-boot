package com.harvey.screen.api;

/**
 * 信发消息类型
 *
 * @author Harvey
 */
public enum ScreenMessageType {

    /** 设备登录(携带设备编号与令牌) */
    LOGIN((byte) 1),
    /** 设备心跳 */
    HEARTBEAT((byte) 2),
    /** 服务端下发指令 */
    COMMAND((byte) 3),
    /** 设备主动上报(状态/事件/截屏回传等) */
    REPORT((byte) 4),
    /** 指令应答 */
    ACK((byte) 5),
    /** 媒体/大消息通知 */
    MEDIA_NOTIFY((byte) 6);

    private final byte code;

    ScreenMessageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ScreenMessageType of(byte code) {
        for (ScreenMessageType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知信发消息类型: " + code);
    }
}
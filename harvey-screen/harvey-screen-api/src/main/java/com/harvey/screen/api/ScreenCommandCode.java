package com.harvey.screen.api;

/**
 * 信发指令编码(服务端下发，设备端根据编码分发处理)
 *
 * @author Harvey
 */
public final class ScreenCommandCode {

    /** 开机 */
    public static final int POWER_ON = 1001;
    /** 关机 */
    public static final int POWER_OFF = 1002;
    /** 重启 */
    public static final int REBOOT = 1003;

    /** 音量加 */
    public static final int VOLUME_UP = 2001;
    /** 音量减 */
    public static final int VOLUME_DOWN = 2002;

    /** 截屏 */
    public static final int SCREENSHOT = 3001;

    /** 滚动字幕 */
    public static final int MARQUEE = 4001;

    /** 媒体推送 */
    public static final int MEDIA_PUSH = 5001;

    /** 查询设备状态 */
    public static final int QUERY_STATUS = 9001;

    private ScreenCommandCode() {
    }
}
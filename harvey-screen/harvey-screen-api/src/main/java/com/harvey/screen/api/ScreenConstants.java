package com.harvey.screen.api;

/**
 * 信发协议常量
 * <p>
 * 报文格式(全大端)：
 * <pre>
 * | magic(4) | version(1) | messageType(1) | seq(8) | cmdCode(4) | length(4) | body(变长) | crc32(4) |
 * </pre>
 * 固定报文头 22 字节，正文为 JSON 字节数组，末尾追加整帧 CRC32 校验。
 *
 * @author Harvey
 */
public final class ScreenConstants {

    /** 魔数 'H' 'S' 'M' 'T' */
    public static final int MAGIC = 0x48534D54;

    /** 协议版本 */
    public static final byte VERSION = 1;

    /** 固定报文头长度(不含正文与 CRC)：magic4 + version1 + type1 + seq8 + cmdCode4 + length4 */
    public static final int HEADER_LENGTH = 22;

    /** CRC32 校验字节数 */
    public static final int CRC_LENGTH = 4;

    /** 默认服务端口 */
    public static final int DEFAULT_PORT = 10000;

    /** 默认最大报文长度(10MB) */
    public static final int DEFAULT_MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    private ScreenConstants() {
    }
}
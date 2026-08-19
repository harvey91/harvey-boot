package com.harvey.screen.api;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * 信发报文编解码
 * <p>
 * 报文格式(全大端)：
 * <pre>
 * | magic(4) | version(1) | messageType(1) | seq(8) | cmdCode(4) | length(4) | body(变长) | crc32(4) |
 * </pre>
 * CRC32 覆盖报文头 + 正文。
 *
 * @author Harvey
 */
public final class ScreenCodec {

    private ScreenCodec() {
    }

    /**
     * 编码报文为完整帧字节数组
     */
    public static byte[] encode(ScreenMessage message) {
        byte[] body = message.getBody() == null ? new byte[0] : message.getBody();
        if (body.length > ScreenConstants.DEFAULT_MAX_FRAME_LENGTH) {
            throw new ScreenCodecException("报文正文超过上限: " + body.length);
        }
        byte[] header = buildHeader(message, body.length);
        int crc = crc32(header, body);

        message.setLength(body.length);
        message.setCrc32(crc);

        byte[] frame = new byte[ScreenConstants.HEADER_LENGTH + body.length + ScreenConstants.CRC_LENGTH];
        System.arraycopy(header, 0, frame, 0, header.length);
        System.arraycopy(body, 0, frame, header.length, body.length);
        writeInt(frame, header.length + body.length, crc);
        return frame;
    }

    /**
     * 解码完整帧为报文(校验魔数/版本/长度/CRC)
     */
    public static ScreenMessage decode(byte[] frame) {
        int total = ScreenConstants.HEADER_LENGTH + ScreenConstants.CRC_LENGTH;
        if (frame.length < total) {
            throw new ScreenCodecException("帧长度不足: " + frame.length);
        }
        validateHeader(frame);

        int length = readLength(frame);
        if (ScreenConstants.HEADER_LENGTH + length + ScreenConstants.CRC_LENGTH != frame.length) {
            throw new ScreenCodecException("帧长度与正文长度不一致: length=" + length + ", frame=" + frame.length);
        }
        int actualCrc = readInt(frame, ScreenConstants.HEADER_LENGTH + length);
        int expectedCrc = crc32(Arrays.copyOfRange(frame, 0, ScreenConstants.HEADER_LENGTH + length));
        if (actualCrc != expectedCrc) {
            throw new ScreenCodecException("CRC 校验失败: expected=" + expectedCrc + ", actual=" + actualCrc);
        }

        ScreenMessage message = new ScreenMessage();
        message.setMagic(readInt(frame, 0));
        message.setVersion(frame[4]);
        message.setMessageType(frame[5]);
        message.setSeq(readLong(frame, 6));
        message.setCmdCode(readInt(frame, 14));
        message.setLength(length);
        message.setBody(length > 0 ? Arrays.copyOfRange(frame, ScreenConstants.HEADER_LENGTH, ScreenConstants.HEADER_LENGTH + length) : new byte[0]);
        message.setCrc32(actualCrc);
        return message;
    }

    /**
     * 从报文头字节中读取正文长度(供粘包/拆包时计算整帧大小)
     */
    public static int readLength(byte[] header) {
        validateHeader(header);
        return readInt(header, 18);
    }

    /**
     * 校验报文头：长度、魔数、版本
     */
    public static void validateHeader(byte[] header) {
        if (header.length < ScreenConstants.HEADER_LENGTH) {
            throw new ScreenCodecException("报文头长度不足: " + header.length);
        }
        if (readInt(header, 0) != ScreenConstants.MAGIC) {
            throw new ScreenCodecException("魔数不匹配");
        }
        if (header[4] != ScreenConstants.VERSION) {
            throw new ScreenCodecException("协议版本不匹配: " + header[4]);
        }
    }

    private static byte[] buildHeader(ScreenMessage message, int bodyLength) {
        byte[] header = new byte[ScreenConstants.HEADER_LENGTH];
        writeInt(header, 0, message.getMagic() == 0 ? ScreenConstants.MAGIC : message.getMagic());
        header[4] = message.getVersion() == 0 ? ScreenConstants.VERSION : message.getVersion();
        header[5] = message.getMessageType();
        writeLong(header, 6, message.getSeq());
        writeInt(header, 14, message.getCmdCode());
        writeInt(header, 18, bodyLength);
        return header;
    }

    private static int crc32(byte[] header, byte[] body) {
        CRC32 crc = new CRC32();
        crc.update(header);
        crc.update(body);
        return (int) crc.getValue();
    }

    private static int crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return (int) crc.getValue();
    }

    private static void writeInt(byte[] bytes, int offset, int value) {
        ByteBuffer.wrap(bytes, offset, 4).putInt(value);
    }

    private static void writeLong(byte[] bytes, int offset, long value) {
        ByteBuffer.wrap(bytes, offset, 8).putLong(value);
    }

    private static int readInt(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 4).getInt();
    }

    private static long readLong(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 8).getLong();
    }
}
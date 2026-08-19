package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 信发报文
 * <p>
 * 字段布局与 {@link ScreenConstants#HEADER_LENGTH} 一致，正文为 JSON 字节数组。
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenMessage {

    /** 魔数 */
    private int magic;

    /** 协议版本 */
    private byte version;

    /** 消息类型，见 {@link ScreenMessageType} */
    private byte messageType;

    /** 消息序号(指令追踪/应答关联) */
    private long seq;

    /** 指令编码，见 {@link ScreenCommandCode} */
    private int cmdCode;

    /** 正文长度 */
    private int length;

    /** 正文(JSON 字节数组) */
    private byte[] body;

    /** 整帧 CRC32(编码时计算) */
    private int crc32;

    public boolean is(ScreenMessageType type) {
        return this.messageType == type.getCode();
    }

    /**
     * 解析正文为指定消息体
     */
    public <T> T payload(Class<T> type) {
        return ScreenJson.fromBytes(this.body, type);
    }

    /**
     * 构建报文
     */
    public static ScreenMessage of(byte messageType, long seq, int cmdCode, byte[] body) {
        ScreenMessage message = new ScreenMessage();
        message.setMagic(ScreenConstants.MAGIC);
        message.setVersion(ScreenConstants.VERSION);
        message.setMessageType(messageType);
        message.setSeq(seq);
        message.setCmdCode(cmdCode);
        byte[] bytes = body == null ? new byte[0] : body;
        message.setBody(bytes);
        message.setLength(bytes.length);
        return message;
    }

    /**
     * 构建携带 JSON 正文的报文
     */
    public static ScreenMessage of(byte messageType, long seq, int cmdCode, Object payload) {
        return of(messageType, seq, cmdCode, payload == null ? null : ScreenJson.toBytes(payload));
    }

    public static ScreenMessage login(long seq, LoginMessage login) {
        return of(ScreenMessageType.LOGIN.getCode(), seq, 0, login);
    }

    public static ScreenMessage heartbeat(long seq, HeartbeatMessage heartbeat) {
        return of(ScreenMessageType.HEARTBEAT.getCode(), seq, 0, heartbeat);
    }

    public static ScreenMessage ack(long seq, AckMessage ack) {
        return of(ScreenMessageType.ACK.getCode(), seq, 0, ack);
    }

    public static ScreenMessage report(long seq, ReportMessage report) {
        return of(ScreenMessageType.REPORT.getCode(), seq, 0, report);
    }

    public static ScreenMessage command(long seq, int cmdCode, Map<String, Object> params) {
        return of(ScreenMessageType.COMMAND.getCode(), seq, cmdCode, new CommandMessage(params, System.currentTimeMillis()));
    }

    public static ScreenMessage mediaNotify(long seq, MediaNotifyMessage media) {
        return of(ScreenMessageType.MEDIA_NOTIFY.getCode(), seq, 0, media);
    }
}
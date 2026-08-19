package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指令应答消息体
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AckMessage {

    /** 被应答的原始消息序号 */
    private long ackSeq;

    /** 应答状态，见 {@link AckStatus} */
    private int status;

    /** 应答说明 */
    private String message;

    public static AckMessage of(long ackSeq, AckStatus status) {
        return new AckMessage(ackSeq, status.getCode(), status.name());
    }

    public static AckMessage of(long ackSeq, AckStatus status, String message) {
        return new AckMessage(ackSeq, status.getCode(), message);
    }
}
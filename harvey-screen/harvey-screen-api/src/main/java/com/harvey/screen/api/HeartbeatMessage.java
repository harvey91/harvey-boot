package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备心跳消息体
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatMessage {

    /** 设备编号 */
    private String deviceNo;

    /** 客户端时间戳(ms) */
    private long timestamp;
}
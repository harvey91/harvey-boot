package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 服务端下发指令消息体(指令编码在报文头 cmdCode 字段)
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandMessage {

    /** 指令参数 */
    private Map<String, Object> params;

    /** 下发时间戳(ms) */
    private long timestamp;
}
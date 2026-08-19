package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备登录消息体
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginMessage {

    /** 设备编号 */
    private String deviceNo;

    /** 登录令牌(设备先通过 HTTP 认证获取) */
    private String token;

    /** 设备型号 */
    private String model;

    /** 客户端时间戳(ms) */
    private long timestamp;
}
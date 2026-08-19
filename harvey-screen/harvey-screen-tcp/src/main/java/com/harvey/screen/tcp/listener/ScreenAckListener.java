package com.harvey.screen.tcp.listener;

import com.harvey.screen.api.AckMessage;

/**
 * 设备指令应答监听器
 * <p>
 * 由服务模块实现，用于指令状态流转(已发送 -> 已执行/失败)。
 *
 * @author Harvey
 */
public interface ScreenAckListener {

    /**
     * 收到设备指令应答
     *
     * @param deviceNo 设备编号
     * @param ack      应答消息体(ackSeq 为被应答指令的报文序号)
     */
    void onAck(String deviceNo, AckMessage ack);
}
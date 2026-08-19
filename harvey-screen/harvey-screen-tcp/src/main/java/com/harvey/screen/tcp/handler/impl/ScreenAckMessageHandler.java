package com.harvey.screen.tcp.handler.impl;

import com.harvey.screen.api.AckMessage;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import com.harvey.screen.tcp.ScreenChannelAttributes;
import com.harvey.screen.tcp.handler.ScreenMessageHandler;
import com.harvey.screen.tcp.listener.ScreenAckListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设备指令应答处理：记录 ACK 结果，并通知 {@link ScreenAckListener} 做指令状态流转
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenAckMessageHandler implements ScreenMessageHandler {

    private final List<ScreenAckListener> ackListeners;

    public ScreenAckMessageHandler(List<ScreenAckListener> ackListeners) {
        this.ackListeners = ackListeners;
    }

    @Override
    public byte type() {
        return ScreenMessageType.ACK.getCode();
    }

    @Override
    public void handle(ScreenMessage message, ChannelHandlerContext ctx) {
        AckMessage ack = message.payload(AckMessage.class);
        String deviceNo = ctx.channel().attr(ScreenChannelAttributes.DEVICE_NO).get();
        log.info("收到设备 ACK: deviceNo={}, ackSeq={}, status={}, msg={}",
                deviceNo, ack.getAckSeq(), ack.getStatus(), ack.getMessage());
        for (ScreenAckListener listener : ackListeners) {
            try {
                listener.onAck(deviceNo, ack);
            } catch (Exception e) {
                log.error("ACK 监听器处理失败: deviceNo={}, ackSeq={}", deviceNo, ack.getAckSeq(), e);
            }
        }
    }
}
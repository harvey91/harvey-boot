package com.harvey.screen.tcp.handler.impl;

import com.harvey.screen.api.ReportMessage;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import com.harvey.screen.tcp.ScreenChannelAttributes;
import com.harvey.screen.tcp.handler.ScreenMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 设备上报处理：状态变化/事件/截屏回传等(骨架阶段仅记录日志)
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenReportMessageHandler implements ScreenMessageHandler {

    @Override
    public byte type() {
        return ScreenMessageType.REPORT.getCode();
    }

    @Override
    public void handle(ScreenMessage message, ChannelHandlerContext ctx) {
        ReportMessage report = message.payload(ReportMessage.class);
        String deviceNo = ctx.channel().attr(ScreenChannelAttributes.DEVICE_NO).get();
        log.info("收到设备上报: deviceNo={}, reportType={}, data={}",
                deviceNo, report.getReportType(), report.getData());
    }
}
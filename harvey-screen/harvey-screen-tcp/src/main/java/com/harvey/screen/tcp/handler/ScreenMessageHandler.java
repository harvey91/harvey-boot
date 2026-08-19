package com.harvey.screen.tcp.handler;

import com.harvey.screen.api.ScreenMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 信发消息处理器接口
 * <p>
 * 通过 {@link #type()} 与报文消息类型关联，由 {@link com.harvey.screen.tcp.handler.ScreenServerHandler} 分发。
 *
 * @author Harvey
 */
public interface ScreenMessageHandler {

    /**
     * 支持的消息类型，见 {@link com.harvey.screen.api.ScreenMessageType}
     */
    byte type();

    /**
     * 处理消息
     */
    void handle(ScreenMessage message, ChannelHandlerContext ctx) throws Exception;
}
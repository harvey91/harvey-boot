package com.harvey.screen.tcp.codec;

import com.harvey.screen.api.ScreenCodec;
import com.harvey.screen.api.ScreenMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 信发报文编码器
 *
 * @author Harvey
 */
public class ScreenMessageEncoder extends MessageToByteEncoder<ScreenMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ScreenMessage msg, ByteBuf out) {
        out.writeBytes(ScreenCodec.encode(msg));
    }
}
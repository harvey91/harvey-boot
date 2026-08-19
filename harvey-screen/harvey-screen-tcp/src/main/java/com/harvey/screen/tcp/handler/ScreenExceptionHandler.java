package com.harvey.screen.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 信发连接异常兜底处理
 *
 * @author Harvey
 */
@Slf4j
public class ScreenExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("信发连接异常, 关闭连接: remote={}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
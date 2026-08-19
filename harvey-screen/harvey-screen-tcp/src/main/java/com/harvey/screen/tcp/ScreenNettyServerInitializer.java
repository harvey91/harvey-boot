package com.harvey.screen.tcp;

import com.harvey.screen.tcp.codec.ScreenMessageDecoder;
import com.harvey.screen.tcp.codec.ScreenMessageEncoder;
import com.harvey.screen.tcp.handler.ScreenExceptionHandler;
import com.harvey.screen.tcp.handler.ScreenMessageHandler;
import com.harvey.screen.tcp.handler.ScreenServerHandler;
import com.harvey.screen.tcp.listener.ScreenSessionListener;
import com.harvey.screen.tcp.property.ScreenNettyProperties;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 信发服务端 Channel 初始化：编解码 -> 空闲检测 -> 核心处理 -> 异常兜底
 *
 * @author Harvey
 */
@Component
public class ScreenNettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ScreenNettyProperties properties;
    private final ScreenSessionManager sessionManager;
    private final List<ScreenSessionListener> sessionListeners;
    private final List<ScreenMessageHandler> handlers;

    public ScreenNettyServerInitializer(ScreenNettyProperties properties,
                                        ScreenSessionManager sessionManager,
                                        List<ScreenSessionListener> sessionListeners,
                                        List<ScreenMessageHandler> handlers) {
        this.properties = properties;
        this.sessionManager = sessionManager;
        this.sessionListeners = sessionListeners;
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast("screenFrameDecoder", new ScreenMessageDecoder(properties.getMaxFrameLength()))
                .addLast("screenFrameEncoder", new ScreenMessageEncoder())
                .addLast("screenIdleStateHandler", new IdleStateHandler(
                        properties.getReaderIdleSeconds(),
                        properties.getWriterIdleSeconds(),
                        properties.getAllIdleSeconds()))
                .addLast("screenServerHandler", new ScreenServerHandler(properties, sessionManager, sessionListeners, handlers))
                .addLast("screenExceptionHandler", new ScreenExceptionHandler());
    }
}
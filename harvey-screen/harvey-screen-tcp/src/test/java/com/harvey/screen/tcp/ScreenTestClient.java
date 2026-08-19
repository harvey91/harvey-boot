package com.harvey.screen.tcp;

import com.harvey.screen.api.ScreenConstants;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import com.harvey.screen.tcp.codec.ScreenMessageDecoder;
import com.harvey.screen.tcp.codec.ScreenMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 测试用信发客户端(复用服务端协议编解码)
 *
 * @author Harvey
 */
public class ScreenTestClient {

    private final EventLoopGroup group = new NioEventLoopGroup(1);
    private final BlockingQueue<ScreenMessage> received = new LinkedBlockingQueue<>();
    private Channel channel;

    public void connect(InetSocketAddress address) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new ScreenMessageDecoder(ScreenConstants.DEFAULT_MAX_FRAME_LENGTH))
                                .addLast(new ScreenMessageEncoder())
                                .addLast(new SimpleChannelInboundHandler<ScreenMessage>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ScreenMessage msg) {
                                        received.offer(msg);
                                    }
                                });
                    }
                });
        channel = bootstrap.connect(address).syncUninterruptibly().channel();
    }

    public void send(ScreenMessage message) {
        channel.writeAndFlush(message).syncUninterruptibly();
    }

    /**
     * 等待指定类型的消息
     */
    public ScreenMessage await(ScreenMessageType type, long timeoutMs) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            long remaining = deadline - System.currentTimeMillis();
            ScreenMessage message = received.poll(Math.max(1, remaining), TimeUnit.MILLISECONDS);
            if (message == null) {
                return null;
            }
            if (message.is(type)) {
                return message;
            }
            received.offer(message);
        }
        return null;
    }

    public void close() throws InterruptedException {
        if (channel != null && channel.isActive()) {
            channel.close().syncUninterruptibly();
        }
        group.shutdownGracefully(0, 1, TimeUnit.SECONDS).syncUninterruptibly();
    }
}
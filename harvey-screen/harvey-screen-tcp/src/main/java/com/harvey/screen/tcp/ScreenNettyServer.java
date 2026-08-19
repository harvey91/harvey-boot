package com.harvey.screen.tcp;

import com.harvey.screen.tcp.property.ScreenNettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 信发 Netty 服务端：随 Spring 容器启动/停止
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenNettyServer {

    private final ScreenNettyProperties properties;
    private final ScreenNettyServerInitializer initializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public ScreenNettyServer(ScreenNettyProperties properties, ScreenNettyServerInitializer initializer) {
        this.properties = properties;
        this.initializer = initializer;
    }

    @PostConstruct
    public void start() {
        if (!properties.isEnabled()) {
            log.warn("信发 TCP 服务未启用");
            return;
        }
        int bossThreads = properties.getBossThreads() > 0 ? properties.getBossThreads() : 1;
        int workerThreads = properties.getWorkerThreads() > 0
                ? properties.getWorkerThreads()
                : Math.max(1, Runtime.getRuntime().availableProcessors() * 2);

        bossGroup = new NioEventLoopGroup(bossThreads);
        workerGroup = new NioEventLoopGroup(workerThreads);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, properties.getBacklog())
                .childOption(ChannelOption.TCP_NODELAY, properties.isTcpNoDelay())
                .childOption(ChannelOption.SO_KEEPALIVE, properties.isKeepAlive())
                .childHandler(initializer);

        InetSocketAddress address = properties.getHost() == null
                ? new InetSocketAddress(properties.getPort())
                : new InetSocketAddress(properties.getHost(), properties.getPort());
        ChannelFuture future = bootstrap.bind(address).syncUninterruptibly();
        serverChannel = future.channel();
        log.info("信发 TCP 服务启动成功, 端口={}", getPort());
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close().awaitUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().awaitUninterruptibly();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().awaitUninterruptibly();
        }
        log.info("信发 TCP 服务已停止");
    }

    /**
     * 实际监听端口(配置 port=0 时返回随机端口)
     */
    public int getPort() {
        if (serverChannel != null && serverChannel.localAddress() instanceof InetSocketAddress address) {
            return address.getPort();
        }
        return properties.getPort();
    }
}
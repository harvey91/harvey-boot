package com.harvey.screen.cluster;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 信发集群自动配置：绑定配置 + 注册跨节点指令转发订阅容器
 *
 * @author Harvey
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ScreenClusterProperties.class)
public class ScreenClusterAutoConfiguration {

    private final RedisConnectionFactory connectionFactory;
    private final ScreenClusterProperties props;
    private final ScreenClusterSessionRegistry registry;

    /**
     * 跨节点指令转发订阅容器：设备所在节点收到并确认本机会话后下发
     */
    @Bean
    public RedisMessageListenerContainer screenCommandListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        if (props.isEnabled()) {
            container.addMessageListener(
                    (message, pattern) -> {
                        byte[] body = message.getBody();
                        if (body == null || body.length == 0) {
                            return;
                        }
                        registry.handleForwardMessage(new String(body, java.nio.charset.StandardCharsets.UTF_8));
                    },
                    new ChannelTopic(props.getCommandChannel()));
            log.info("信发集群指令转发订阅已注册: channel={}", props.getCommandChannel());
        }
        return container;
    }
}
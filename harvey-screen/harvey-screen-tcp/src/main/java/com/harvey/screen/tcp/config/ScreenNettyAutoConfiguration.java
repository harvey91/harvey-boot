package com.harvey.screen.tcp.config;

import com.harvey.screen.tcp.property.ScreenNettyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 信发 TCP 服务自动配置
 *
 * @author Harvey
 */
@Configuration
@EnableConfigurationProperties(ScreenNettyProperties.class)
public class ScreenNettyAutoConfiguration {
}
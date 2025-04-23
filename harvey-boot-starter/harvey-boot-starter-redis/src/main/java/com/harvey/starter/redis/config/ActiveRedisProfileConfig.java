package com.harvey.starter.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Harvey
 * @since 2025-04-16 16:07
 **/
@Slf4j
//@AutoConfiguration
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class ActiveRedisProfileConfig implements EnvironmentPostProcessor {

    static {
        System.out.println("ActiveRedisProfileConfig running");
    }
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("Active Redis profile configuration loaded.");
        environment.addActiveProfile("redis");
    }
}

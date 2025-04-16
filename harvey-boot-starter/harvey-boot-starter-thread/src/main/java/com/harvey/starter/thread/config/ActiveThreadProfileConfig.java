package com.harvey.starter.thread.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Harvey
 * @since 2025-04-16 21:31
 **/
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ActiveThreadProfileConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        environment.addActiveProfile("thread");
    }
}

package com.harvey.starter.minio.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Harvey
 * @since 2025-04-16 16:07
 **/
@Slf4j
public class ActiveMinioProfileConfig implements EnvironmentPostProcessor, Ordered {

    static {
        System.out.println("ActiveMinioProfileConfig running");
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("Active Minio profile configuration loaded.");
        environment.addActiveProfile("minio");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

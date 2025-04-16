package com.harvey.starter.mysql.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Harvey
 * @since 2025-04-16 16:07
 **/
@Slf4j
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class ActiveMysqlProfileConfig implements EnvironmentPostProcessor {

    static {
        System.out.println("ActiveMysqlProfileConfig running");
    }
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("Active Mysql profile configuration loaded.");
        environment.addActiveProfile("mysql");
    }
}

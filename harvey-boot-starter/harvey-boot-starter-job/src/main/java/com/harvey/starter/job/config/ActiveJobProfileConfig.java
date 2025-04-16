package com.harvey.starter.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Harvey
 * @since 2025-04-16 21:31
 **/
@Slf4j
@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ActiveJobProfileConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        environment.addActiveProfile("job");
    }
}

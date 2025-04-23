package com.harvey.starter.thread.config;

import com.harvey.starter.thread.property.ThreadProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @author Harvey
 * @date 2024-11-25 11:36
 **/
@Configuration
@EnableAsync
@EnableConfigurationProperties(ThreadProperties.class)
public class ThreadPoolConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor(ThreadProperties threadProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(threadProperties.getCorePoolSize());
        // 配置最大线程数
        executor.setMaxPoolSize(threadProperties.getMaxPoolSize());
        // 配置队列大小
        executor.setQueueCapacity(threadProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadProperties.getKeepAliveSeconds());
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(threadProperties.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }
}

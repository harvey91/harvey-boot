package com.harvey.core.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 多线程参数
 * @author Harvey
 * @date 2024-11-25 12:09
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "executor")
public class ThreadProperties {

    /**
     * 核心线程数：线程池创建时候初始化的线程数
     */
    private int corePoolSize = 5;

    /**
     * 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     */
    private int maxPoolSize = 10;

    /**
     * 缓冲队列100：用来缓冲执行任务的队列
     */
    private int queueCapacity = 100;

    /**
     * 允许线程的空闲时间(单位：秒)：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
     */
    private int keepAliveSeconds = 10;

    /**
     * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
     */
    private String threadNamePrefix = "thread-";
}

package com.harvey.starter.job.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Harvey
 * @since 2025-04-16 22:08
 **/
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    private String adminAddresses;
    private String accessToken;
    private String appName;
    private String address;
    private String ip;
    private int executorPort;
    private String logPath;
    private int logRetentionDays;
}

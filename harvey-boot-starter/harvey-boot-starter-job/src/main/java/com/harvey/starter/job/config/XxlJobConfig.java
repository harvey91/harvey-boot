package com.harvey.starter.job.config;

import com.harvey.starter.job.property.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Harvey
 * @date 2024-11-25 14:48
 **/
@Slf4j
@AutoConfiguration
public class XxlJobConfig {

    private final XxlJobProperties xxlJobProperties;

    public XxlJobConfig(XxlJobProperties xxlJobProperties) {
        this.xxlJobProperties = xxlJobProperties;
    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutorPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}

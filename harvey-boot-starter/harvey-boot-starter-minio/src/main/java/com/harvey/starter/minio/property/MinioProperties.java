package com.harvey.starter.minio.property;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Harvey
 * @since 2025-04-23 20:30
 **/
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 外网地址
     */
    private String endpoint;

    /**
     * 内网地址，可提升上传/下载速度
     */
    private String innerEndpoint;

    /**
     * 默认存储桶名称
     */
    private String bucketName;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;


    /**
     * 获取内网端点
     *
     * @return {@link String}
     */
    public String getInnerEndpoint() {
        if (!StringUtils.hasText(innerEndpoint)) {
            log.warn("未设置内网地址，默认使用公共地址。");
            return endpoint;
        }
        return innerEndpoint;
    }}

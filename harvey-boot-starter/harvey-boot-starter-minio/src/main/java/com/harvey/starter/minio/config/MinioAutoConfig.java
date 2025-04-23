package com.harvey.starter.minio.config;

import com.harvey.starter.minio.property.MinioProperties;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author Harvey
 * @since 2025-04-23 20:28
 **/
@AutoConfiguration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(prefix = "minio", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class MinioAutoConfig {

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(minioProperties.getPublicEndpoint());
        if (StringUtils.hasLength(minioProperties.getAccessKey()) && StringUtils.hasLength(minioProperties.getSecretKey())) {
            builder.credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
        }
        return builder.build();
    }

    @Bean(name = "privateMinioClient")
    public MinioClient privateMinioClient(MinioProperties minioProperties) {
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(minioProperties.getPrivateEndpoint());
        if (StringUtils.hasLength(minioProperties.getAccessKey()) && StringUtils.hasLength(minioProperties.getSecretKey())) {
            builder.credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
        }
        return builder.build();
    }
}

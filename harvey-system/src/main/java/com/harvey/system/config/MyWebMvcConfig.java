package com.harvey.system.config;

import com.harvey.core.storage.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Harvey
 * @date 2024-12-11 11:27
 **/
@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfig implements WebMvcConfigurer {
    private final StorageProperties storageProperties;

    /**
     * 映射文件路径配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/fetch/**")
                .addResourceLocations("file:" + storageProperties.getLocal().getStoragePath() + "/");
    }
}

package com.harvey.system.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harvey
 * @date 2024-11-05 14:50
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String tokenStartWith;

    private String base64Secret;
}

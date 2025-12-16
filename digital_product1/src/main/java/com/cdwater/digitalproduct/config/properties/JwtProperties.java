package com.cdwater.digitalproduct.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT属性配置
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret;
    private long adminExpiration;
    private long shopExpiration;
    private long userExpiration;
    private String tokenName;
}

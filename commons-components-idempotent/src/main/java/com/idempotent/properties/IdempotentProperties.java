package com.idempotent.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Dingkeke
 * @date 2021-08-10 9:54
 */
@ConfigurationProperties(prefix = IdempotentProperties.PREFIX)
@RefreshScope
public class IdempotentProperties {

    public static final String PREFIX="components.idempotent";

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    private String cacheName;
}

package com.idempotent.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Dingkeke
 * @date 2021-08-10 9:54
 */
@ConfigurationProperties(prefix = IdempotentProperties.PREFIX)
@Setter
@Getter
@RefreshScope
public class IdempotentProperties {

    public static final String PREFIX="components.idempotent";

    private String cacheName;
}

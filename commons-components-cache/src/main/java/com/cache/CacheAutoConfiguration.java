package com.cache;

import com.cache.aspect.CacheAspect;
import com.cache.client.CacheClient;
import com.cache.client.CacheClientImpl;
import com.cache.client.CacheUtil;
import com.cache.client.CaffeineClient;
import com.cache.client.config.CaffeineProperties;
import com.cache.config.ExcludeCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author caijie
 */

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({CaffeineProperties.class})
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CacheAspect.class)
    public CacheAspect cacheAspect() {
        return new CacheAspect();
    }

    @Bean
    public CacheUtil cacheUtil() {
        return new CacheUtil();
    }

    @Bean
    public CaffeineClient caffeineUtil(CaffeineProperties caffeineProperties) {
        return new CaffeineClient(caffeineProperties);
    }

    @Bean
    @ConditionalOnMissingBean(CacheClient.class)
    public CacheClient cacheClient() {
        return new CacheClientImpl();
    }

    @Bean
    public RenaultProperties redisClient() {
        return new RenaultProperties();
    }

    @Bean
    public ExcludeCache excludeCache() {
        return new ExcludeCache();
    }
}

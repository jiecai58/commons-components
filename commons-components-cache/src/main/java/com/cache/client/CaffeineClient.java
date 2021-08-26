package com.cache.client;

import com.cache.client.config.CaffeineProperties;
import org.springframework.stereotype.Component;

/**
 * @author caijie
 */
@Component
public class CaffeineClient {

    /**
     * 存储缓存
     */
    private final Cache<Object, Object> BUILD;

    public CaffeineClient(CaffeineProperties caffeineProperties) {
        BUILD = Caffeine.newBuilder().initialCapacity(caffeineProperties.getInitialCapacity())
                .maximumSize(caffeineProperties.getMaximumSize()).recordStats()
                .expireAfterWrite(caffeineProperties.getDuration(), caffeineProperties.getUnit()).build();
    }

    public Object get(String key) {
        return BUILD.getIfPresent(key);
    }

    public void set(String key, String value) {
        BUILD.put(key, value);
    }

    public void del(String key) {
        BUILD.invalidate(key);
    }

}

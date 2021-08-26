package com.cache.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author caijie
 */
@Component
public class CacheUtil {

    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static final String CAFFEINE_ENABLE_KEY = "caffeine_enable";

    ThreadLocal<Boolean> caffeineEnableStatus = new ThreadLocal<>();

    @Resource
    private com.cache.client.CaffeineClient caffeineClient;

    @Resource
    private CacheClient cacheClient;

    public String get(String configName, String key, boolean degraded) {
        if (caffeineClosedState()) {
            try {
                return (String) cacheClient.getCache(configName, key);
            } catch (Exception e) {
                openCaffeineEnable();
                logger.error("renault error:{}", e.getMessage());
                return null;
            }
        }
        if (!degraded) {
            return null;
        }
        return (String) caffeineClient.get(key);
    }

    public void setDate(String configName, String key, String value, Long timeOut, boolean degraded, long maxSize) {
        if (sizeWithinRange(value, maxSize)) {
            this.set(configName, key, value, timeOut, degraded);
        }
    }

    private void set(String configName, String key, String value, Long timeOut, boolean degraded) {
        if (caffeineClosedState()) {
            try {
                cacheClient.setCache(configName, key, value, timeOut);
            } catch (Exception e) {
                openCaffeineEnable();
                logger.error("renault error: {}", e.getMessage());
            }
        } else if (degraded) {
            caffeineClient.set(key, value);
        }
    }

    public void delete(String configName, String key) {
        if (caffeineClosedState()) {
            try {
                cacheClient.deleteCache(configName, key);
            } catch (Exception e) {
                openCaffeineEnable();
                logger.error("renault error:{}", e.getMessage());
            }
        } else {
            caffeineClient.del(key);
        }
    }

    private boolean caffeineClosedState() {
        if (caffeineEnableStatus.get() == null) {
            setCaffeineEnableStatus();
        }
        return caffeineEnableStatus.get();
    }

    private void openCaffeineEnable() {
        caffeineClient.set(CAFFEINE_ENABLE_KEY, "0");
        setCaffeineEnableStatus();
    }

    private void setCaffeineEnableStatus() {
        caffeineEnableStatus.set(caffeineClient.get(CAFFEINE_ENABLE_KEY) == null);
    }

    private boolean sizeWithinRange(String data, long maxSize) {
        //单个key缓存大小限制暂时未实现
        return (maxSize >= 0L && data.length() > 0L);
    }

    public void removeCaffeineEnable() {
        caffeineEnableStatus.remove();
    }
}

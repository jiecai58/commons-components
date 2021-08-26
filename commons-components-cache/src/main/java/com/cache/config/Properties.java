package com.cache.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caijie
 */
@Component
public class Properties {

    @Resource
    private ConfigurableEnvironment environment;

    private String prefix = "xx.";

    private static final Map<String, Object> CACHE_PROPERTIES = new ConcurrentHashMap<>();

    /**
     * 如果读的是本地配置文件，推荐使用此方法，把配置读入内存
     * 如果配置是在apollo，不推荐此方法，apollo配置发布自动推送，可以实现动态变更，速度比此方法慢甚少
     */
    public Map<String, Object> getProperties() {
        if (CACHE_PROPERTIES.isEmpty()) {
            for (PropertySource<?> source : environment.getPropertySources()) {
                if (source instanceof EnumerablePropertySource) {
                    for (String name : ((EnumerablePropertySource<?>) source)
                            .getPropertyNames()) {
                        if (name != null && name.startsWith(prefix)) {
                            String subKey = name.substring(prefix.length());
                            CACHE_PROPERTIES.put(subKey, environment.getProperty(name));
                        }
                    }
                }
            }
        }
        return CACHE_PROPERTIES;
    }
}

package com.cache.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author caijie
 */
@Component
public class ExcludeCache {

    private static final String EXCLUDED_CACHE_KEY = "cache.exclude.key";

    @Resource
    private ConfigurableEnvironment environment;

    private String getProperty() {
        return (String)environment.getProperty(EXCLUDED_CACHE_KEY);
    }

    public boolean execute(String key) {
        String name = getProperty();
        if (StringUtils.hasText(name)) {
            List<String> excludeKeyList = Arrays.asList(name.split(","));
            return excludeKeyList.contains(key);
        }
        return false;
    }
}

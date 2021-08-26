package com.cache.client;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author caijie
 */
public interface CacheClient {

    Boolean setNx(String configName, String key, String value, Long timeOut) throws Exception;

    Object getCache(String configName, String key) throws Exception;

    Boolean setCache(String configName, String key, String value, Long timeOut) throws Exception;

    Boolean deleteCache(String configName, String key) throws Exception;

    Map<String, String> multiGet(String configName, Set<String> keys) throws Exception;

    Boolean multiSet(String configName, Map<String, String> map, Long timeOut, TimeUnit timeUnit) throws Exception;
}

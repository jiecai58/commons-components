package com.cache.client;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author caijie
 */
@Service
public class CacheClientImpl implements CacheClient {

    @Resource
    private RedisUtils redisUtils;

    static final Random RANDOM = new Random();
    @Override
    public Boolean setNx(String configName, String key, String value, Long timeOut) throws Exception {
        return redisUtils.setnx(key, value);
        //setIfAbsent(key, value, timeOut, TimeUnit.SECONDS);
    }

    @Override
    public Object getCache(String configName, String key) throws Exception {
        return redisUtils.get(key);
    }

    @Override
    public Boolean setCache(String configName, String key, String value, Long timeOut) throws Exception {
        if (timeOut == 1) {
            redisUtils.set(key, value);
        } else {
            redisUtils.setEx(key, value, timeOut + RANDOM.nextInt(10), TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public Boolean deleteCache(String configName, String key) throws Exception {
         redisUtils.delete(key);
         return true;
    }

    @Override
    public Map<String, String> multiGet(String configName, Set<String> keys) throws Exception {
         redisUtils.multiGet(keys);
         return null;
    }

    @Override
    public Boolean multiSet(String configName, Map<String, String> map, Long timeOut, TimeUnit timeUnit) throws Exception {
        //multiSet(map, timeOut, timeUnit);
        redisUtils.multiSet(map);
        return null;
    }


}

package com.cache.client;

import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

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
    private RedissonClient redissonClient;

    static final Random RANDOM = new Random();

    @Override
    public Boolean setNx(String configName, String key, String value, Long timeOut) {
        RFuture<Boolean> booleanRFuture = redissonClient.getBucket(key).trySetAsync(value, timeOut, TimeUnit.SECONDS);
        return booleanRFuture.isSuccess();
    }

    @Override
    public Object getCache(String configName, String key) {
        return redissonClient.getBucket(key).get();
    }

    @Override
    public Boolean setCache(String configName, String key, String value, Long timeOut) {
        if (timeOut == 1) {
            redissonClient.getBucket(key).set(value);
        } else {
            redissonClient.getBucket(key).set(value, timeOut + RANDOM.nextInt(10), TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public Boolean deleteCache(String configName, String key) {
        return redissonClient.getBucket(key).delete();
    }

    @Override
    public Map<String, String> multiGet(String configName, Set<String> keys) {
        return null;
    }

    @Override
    public Boolean multiSet(String configName, Map<String, String> map, Long timeOut, TimeUnit timeUnit) {
        //multiSet(map, timeOut, timeUnit);
        return null;
    }


}

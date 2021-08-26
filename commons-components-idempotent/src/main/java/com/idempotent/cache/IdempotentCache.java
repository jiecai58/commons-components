package com.idempotent.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author Dingkeke
 * @date 2021-08-09 15:49
 */
public interface IdempotentCache {

    /**
     * 删除一个key
     *
     * @param key
     * @return
     */
    boolean del(String key);


    /**
     * 设置一个值
     *
     * @param key
     * @param value
     * @param expireAfterWrite
     * @param timeUnit
     * @return
     */
    boolean setIfAbsent(String key, String value, long expireAfterWrite, TimeUnit timeUnit);

    /**
     * 使用key获取value
     *
     * @param key
     * @return
     */
    String get(String key);
}

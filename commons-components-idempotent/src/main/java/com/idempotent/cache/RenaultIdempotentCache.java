package com.idempotent.cache;

import com.idempotent.exception.IdempotentException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Renault 缓存实现
 * @author Dingkeke
 * @date 2021-08-09 15:56
 */
@Slf4j
public class RenaultIdempotentCache implements IdempotentCache{
    RedisCacheClient<String,String> client ;

    public RenaultIdempotentCache(String cacheName){
        log.info("初始化Renault缓存,cacheName [{}]",cacheName);
        this.client = RenaultRedisManager.getClient(cacheName);
    }


    @Override
    public String get(String key) {
        try {
            return client.get(key);
        }catch (KVException e){
            log.error("从Renault 缓存中获取Key [{}] 失败",key,e);
            throw new IdempotentException("获取缓存信息失败",e);
        }
    }

    @Override
    public boolean del(String key) {
        try {
            log.info("删除缓存key [{}]" ,key);

            return client.delete(key);

        }catch (KVException e){
            log.error("从Renault 缓存中删除Key [{}] 失败",key,e);
            throw new IdempotentException("删除缓存信息失败",e);
        }

    }

    @Override
    public boolean setIfAbsent(String key, String value, long expireAfterWrite, TimeUnit timeUnit) {
        try {
            log.info("设置缓存[ifabsent]key [{}]" ,key);

            return client.setIfAbsent(key,value,expireAfterWrite,timeUnit);

        }catch (KVException e){
            log.error("Renault 设置缓存[ifabsent] Key [{}] 失败",key,e);
            throw new IdempotentException("设置缓存信息失败",e);
        }
    }
}

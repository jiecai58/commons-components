package com.cache.aspect;


import com.cache.annotation.CachePut;
import com.cache.annotation.Cacheable;
import com.cache.annotation.UnCacheable;
import com.cache.client.CacheUtil;
import com.cache.config.ExcludeCache;
import com.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author caijie
 */
@Component
@Aspect
public class CacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);

    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private ExcludeCache excludeCache;

    @Around("@annotation(dst)")
    public Object doCache(ProceedingJoinPoint joinPoint, Cacheable dst) {
        Object ret = null;
        String redisKey;
        try {
            if (excludeCache.execute(dst.prefix()) || AspectsBase.notMeetConditions(dst.condition(), joinPoint)) {
                return joinPoint.proceed();
            }
            redisKey = AspectsBase.getCacheKey(dst.prefix(), dst.key(), joinPoint);
            String cacheRet = cacheUtil.get(dst.configName(), redisKey, dst.degraded());
            if (StringUtils.isNotBlank(cacheRet)) {
                return AspectsBase.jsonToClass(joinPoint, cacheRet);
            }
            ret = joinPoint.proceed();
            String data = AspectsBase.objectToString(ret);
            cacheUtil.setDate(dst.configName(), redisKey, data, dst.timeout(), dst.degraded(), dst.maxSize());
        } catch (Throwable e) {
            logger.error("cache error:{}", e.getMessage());
        } finally {
            cacheUtil.removeCaffeineEnable();
        }
        return ret;
    }

    /**
     * 请求参数缓存至内存中 eg:写库同时写内存=>解决主从库延时问题,同时降低数据库访问量
     *
     * @param joinPoint joinPoint
     * @param dst       cachePut
     * @return object
     */
    @Around("@annotation(dst)")
    public Object cachePut(ProceedingJoinPoint joinPoint, CachePut dst) {
        Object ret = null;
        String redisKey;
        try {
            ret = joinPoint.proceed();
            if (AspectsBase.notMeetConditions(dst.condition(), joinPoint)) {
                return ret;
            }
            redisKey = AspectsBase.getCacheKey(dst.prefix(), dst.key(), joinPoint);
            Object[] args = joinPoint.getArgs();
            String data = AspectsBase.objectToString(args.length == 1 ? args[0] : args);
            cacheUtil.setDate(dst.configName(), redisKey, data, dst.timeout(), dst.degraded(), dst.maxSize());
        } catch (Throwable e) {
            logger.error("cache error:{}", e.getMessage());
        } finally {
            cacheUtil.removeCaffeineEnable();
        }
        return ret;
    }

    @Around("@annotation(dst)")
    public Object delCache(ProceedingJoinPoint joinPoint, UnCacheable dst) {
        Object ret = null;
        try {
            boolean notMeetConditions = AspectsBase.notMeetConditions(dst.condition(), joinPoint);
            String redisKey = null;
            if (notMeetConditions) {
                redisKey = AspectsBase.getCacheKey(dst.prefix(), dst.key(), joinPoint);
                cacheUtil.delete(dst.configName(), redisKey);
            }
            ret = joinPoint.proceed();
            if (notMeetConditions) {
                cacheUtil.delete(dst.configName(), redisKey);
            }
        } catch (Throwable e) {
            logger.error(e.getMessage());
        } finally {
            cacheUtil.removeCaffeineEnable();
        }
        return ret;

    }


}

package com.idempotent.aspect;


import com.idempotent.annotation.Idempotent;
import com.idempotent.cache.IdempotentCache;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * IdempotentAspect
 */
@Aspect
public class IdempotentAspect {
    private static final Logger logger = LoggerFactory.getLogger(IdempotentAspect.class);

    private static final ThreadLocal<Map<String, Object>> THREAD_CACHE = ThreadLocal.withInitial(HashMap::new);

    private static final String KEY = "key";

    private static final String DELKEY = "delKey";
    /**
     * 默认持有锁的超时时间为5s
     */
    private static final long LOCK_TIME = 5000L;

    @Autowired
    private IdempotentCache idempotentCache;

    @Autowired
    private KeyResolver keyResolver;

    @Pointcut("@annotation(com.idempotent.annotation.Idempotent)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void beforePointCut(JoinPoint joinPoint) throws NoSuchAlgorithmException {
        //获取请求参数
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (!method.isAnnotationPresent(Idempotent.class)) {
            return;
        }
        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        String key;

        // 若没有配置 幂等 标识编号，则使用 url + 参数列表作为区分
        if (idempotent.keys().length==0) {
            String url = request.getRequestURL().toString();
            String argString = Arrays.asList(joinPoint.getArgs()).toString();
            key = url + argString;
        } else {
            // 使用spel 解析参数
            key = keyResolver.resolver(idempotent, joinPoint);
        }

        //为避免key值过长，使用md5
        String md5Key = Md5Util.encrypt(key);
        logger.info("[idempotent] 创建幂等校验key，原key：[{}],md5Key [{}]", key, md5Key);

        long expireTime = idempotent.expireTime();
        String info = idempotent.info();
        TimeUnit timeUnit = idempotent.timeUnit();
        boolean delKey = idempotent.delKey();

        String value = LocalDateTime.now().toString().replace("T", " ");
        boolean v1;
        try {
            if (null != idempotentCache.get(md5Key)) {
                throw new IdempotentException("接口重复请求[idempotent]:" + info);
            }
            v1 = idempotentCache.setIfAbsent(md5Key, value, expireTime, timeUnit);
            if (!v1) {
                throw new IdempotentException("接口重复请求[idempotent]:" + info);
            } else {
                log.info("[idempotent]:has stored key={},value={},expireTime={}/{},now={}", md5Key, value, expireTime,
                        timeUnit, LocalDateTime.now().toString());
            }
        } catch (Exception e) {
            logger.warn("[idempotent] 重复请求，原key：[{}],md5Key [{}]", key, md5Key);
            throw new IdempotentException("[idempotent]:" + info);
        }
        Map<String, Object> map = THREAD_CACHE.get();
        map.put(KEY, md5Key);
        map.put(DELKEY, delKey);
    }

    @After("pointCut()")
    public void afterPointCut(JoinPoint joinPoint) {
        Map<String, Object> map = THREAD_CACHE.get();
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        String key = map.get(KEY).toString();
        boolean delKey = (boolean) map.get(DELKEY);
        if (delKey) {
            idempotentCache.del(key);
            logger.info("[idempotent]:has removed key={}", key);
        }
        THREAD_CACHE.remove();
    }

}
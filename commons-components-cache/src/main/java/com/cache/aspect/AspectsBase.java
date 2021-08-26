package com.cache.aspect;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.cache.interceptor.CacheAspectSupport;
import com.util.StringUtils;
import com.util.json.JsonUtils;
import com.util.typejudgment.TypeJudgment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * cache aspect base
 *
 * @author caijie
 * @date 2019/9/19 14:23
 */
@Component
public class AspectsBase {
    private AspectsBase() {

    }

    public static boolean notMeetConditions(String condition, ProceedingJoinPoint joinPoint) {
        return (StringUtils.isNotNullOrEmpty(condition) && !AspectsBase.isConditionPassing(condition, joinPoint));
    }

    private static boolean isConditionPassing(String condition, ProceedingJoinPoint joinPoint) {
        EvaluationContext evaluationContext = CacheAspectSupport.setEvalContext(joinPoint);
        return CacheAspectSupport.isConditionPassing(evaluationContext, condition);
    }

    public static String getCacheKey(String prefix, String key, ProceedingJoinPoint joinPoint) {
        String cacheKey;
        if (key.isEmpty()) {
            cacheKey = prefix;
        } else {
            cacheKey = assemblyKey(prefix, key, joinPoint);
        }
        if ((!key.isEmpty() && prefix.equals(cacheKey))) {
            throw new IllegalArgumentException("Failed to obtain cache configuration");
        }
        return cacheKey;
    }


    private static String assemblyKey(String prefix, String key, ProceedingJoinPoint joinPoint) {
        return String.format(prefix, getKey(key, joinPoint));
    }

    /**
     * 获取解析的部分key值
     *
     * @param key       key 前缀
     * @param joinPoint jp
     * @return Object
     */
    private static Object getKey(String key, ProceedingJoinPoint joinPoint) {
        EvaluationContext evaluationContext = CacheAspectSupport.setEvalContext(joinPoint);
        Object keyRet = CacheAspectSupport.getKey(evaluationContext, key);
        //类型判断暂不完善

        if (TypeJudgment.isString(keyRet) || TypeJudgment.isInteger(keyRet) || TypeJudgment.isLong(keyRet)) {
            return keyRet;
        } else {
            return keyRet.hashCode();
        }
    }

    public static Object jsonToClass(ProceedingJoinPoint pjp, String cacheRet) {
        Class<Object> type = ((MethodSignature) pjp.getSignature()).getReturnType();
        if (type.equals(String.class)) {
            return cacheRet;
        } else {
            JsonElement parse = new JsonParser().parse(cacheRet);
            if (TypeJudgment.isJsonObject(parse)) {
                return JsonUtils.fromJsonObject(cacheRet, type);
            } else if (TypeJudgment.isJsonArray(parse)) {
                return JsonUtils.fromJsonArray(cacheRet, type);
            } else {
                return cacheRet;
            }
        }
    }

    private static boolean notEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        if (TypeJudgment.isArrayList(obj)) {
            return !((ArrayList) obj).isEmpty();
        }
        return true;
    }

    public static String objectToString(Object ret) {
        String date = null;
        if (AspectsBase.notEmpty(ret)) {
            if (TypeJudgment.isString(ret)) {
                date = ret.toString();
            } else {
                date = JsonUtils.toJson(ret);
            }
        }
        return date;
    }
}

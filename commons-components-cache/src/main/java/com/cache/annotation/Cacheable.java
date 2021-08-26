package com.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author caijie
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * renault 缓存名称
     */
    String configName();

    /**
     * 缓存前缀
     */
    String prefix();

    /**
     * 缓存变量key
     * key = "#user.id + #user.name"
     * key ="#name.concat(#email)"
     */
    String key() default "";

    /**
     * 过期时间
     */
    long timeout() default 1;

    /**
     * eq相等 ne、neq不相等， gt大于， lt小于 gte、ge大于等于 lte、le 小于等于
     * condition = "#user.age < 35"
     * "#age ne 'null'"
     */
    String condition() default "";

    /**
     * 降级
     */
    boolean degraded() default false;

    /**
     * 最大存储长度
     */
    long maxSize() default 0L;

}

package com.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zengfanpan on 2021/3/1 19:16
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnCacheable {

    String configName();

    String prefix();

    /**
     * key = "#user.id + #user.name"
     * key ="#name.concat(#email)"
     */
    String key() default "";

    /**
     * eq相等 ne、neq不相等， gt大于， lt小于 gte、ge大于等于 lte、le 小于等于
     * condition = "#user.age < 35"
     * "#age ne 'null'"
     */
    String condition() default "";
}

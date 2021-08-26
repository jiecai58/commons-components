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
public @interface CachePut {

    String configName();

    String prefix();

    String key() default "";

    long timeout() default 1;

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

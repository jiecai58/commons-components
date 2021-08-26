package com.cache.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * maximumWeight：设置缓存最大权重，设置权重是通过weigher方法， 需要注意的是权重也是限制缓存大小的参数，并不会影响缓存淘汰策略，也不能和maximumSize方法一起使用。
 * weakKeys：将key设置为弱引用，在GC时可以直接淘汰
 * weakValues：将value设置为弱引用，在GC时可以直接淘汰
 * softValues：将value设置为软引用，在内存溢出前可以直接淘汰
 * expireAfterWrite：写入后隔段时间过期
 * expireAfterAccess：访问后隔断时间过期
 * refreshAfterWrite：写入后隔断时间刷新
 * removalListener：缓存淘汰监听器，配置监听器后，每个条目淘汰时都会调用该监听器
 * writer：writer监听器其实提供了两个监听，一个是缓存写入或更新是的write，一个是缓存淘汰时的delete，每个条目淘汰时都会调用该监听器
 * scheduler(Scheduler.systemScheduler()) :需要自定义调度器，用定时任务去主动提前刷新
 * concurrencyLevel：写的并发数 与 weigher 同时使用
 * softValues: 软引用
 * weakKeys: 弱引用
 * weakValues: 弱引用
 * recordStats: 统计的
 *
 * @author caijie
 */

@ConfigurationProperties(prefix = "caffeine.conf")
public class CaffeineProperties {

    /**
     * initialCapacity：初始化个数
     */
    private int initialCapacity = 1024;

    /**
     * maximumSize：设置缓存最大条目数，超过条目则触发回收。
     */
    private int maximumSize = 10240;

    /**
     * 缓存过期时间
     */
    private long duration = 600;

    /**
     * 过期时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

}

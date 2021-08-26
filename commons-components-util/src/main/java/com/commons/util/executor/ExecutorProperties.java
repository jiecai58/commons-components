package com.util.executor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 线程池属性
 * </p>
 *
 * @author caijie
 * @since 2021/3/13 14:43
 */
public class ExecutorProperties {

    /**
     * 核心线程数,线程池保持ALIVE状态线程数
     */
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() >> 0b1;
    /**
     * 线程池最大线程数
     */
    public static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() << 0b1;

    /**
     * 空闲线程回收时间
     */
    public static final int KEEP_ALIVE_TIME = 0b1 << 0b1010;

    /**
     * 线程池等待队列
     */
    public static final int BLOCKING_QUEUE_SIZE = 0b1 << 0b1010;


    /**
     * 线程池维护线程所允许的空闲时间
     */
    public static final int KEEP_ALIVE_SECONDS = 0b1 << 0b1010;

    /**
     * 线程的名称前缀
     */
    public static final String THREAD_NAME_PREFIX = "taskExecutor-";

    /**
     * 拒绝策略{@link ThreadPoolExecutor.CallerRunsPolicy}
     */
    public static final String REJECTION_POLICY_NAME = "CallerRunsPolicy";


}

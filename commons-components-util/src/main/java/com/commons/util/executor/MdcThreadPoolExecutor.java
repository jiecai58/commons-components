package com.util.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * A SLF4J MDC-compatible {@link ThreadPoolExecutor}.
 * <p/>
 * In general, MDC is used to store diagnostic information (e.g. a user's session id) in per-thread variables, to facilitate
 * logging. However, although MDC data is passed to thread children, this doesn't work when threads are reused in a
 * thread pool. This is a drop-in replacement for {@link ThreadPoolExecutor} sets MDC data before each task appropriately.
 * <p/>
 *
 * @author caijie
 * @since 2021/3/13 14:43
 */
public class MdcThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MdcThreadPoolExecutor.class);

    private final boolean useFixedContext;

    private final Map<String, String> fixedContext;

    public static ThreadPoolExecutor ThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        return ThreadPoolExecutor(corePoolSize, maximumPoolSize, null,  null);
    }

    public static ThreadPoolExecutor ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, Long keepAliveTime, TimeUnit unit) {
        return ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, null, null, null);
    }

    /**
     * Pool where task threads always have a specified, fixed MDC.
     */
    public static MdcThreadPoolExecutor newWithFixedMdc(Map<String, String> fixedContext, int corePoolSize,
                                                        int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                                        RejectedExecutionHandler handler) {
        return new MdcThreadPoolExecutor(fixedContext, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private MdcThreadPoolExecutor(Map<String, String> fixedContext, int corePoolSize, int maximumPoolSize,
                                  long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
    }

    /**
     * Thread pool creation, according to the hardware limit the number of threads
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   超出核心线程外的线程存活周期
     * @param workQueue       工具对象
     * @param threadFactory   线程工厂
     * @param handler         拒绝策略
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, Long keepAliveTime, TimeUnit unit,
                                                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                                        RejectedExecutionHandler handler) {

        corePoolSize = Math.min(corePoolSize, ExecutorProperties.CORE_POOL_SIZE);

        maximumPoolSize = Math.min(maximumPoolSize, ExecutorProperties.MAX_POOL_SIZE);
        keepAliveTime = Math.max(ExecutorProperties.KEEP_ALIVE_TIME, keepAliveTime);

        if (workQueue == null) {
            workQueue = new LinkedBlockingDeque<Runnable>(ExecutorProperties.BLOCKING_QUEUE_SIZE);
        }

        if (threadFactory == null) {
            threadFactory = Executors.defaultThreadFactory();
        }

        if (handler == null) {
            handler = new CallerRunsPolicy();
        }
        if (unit == null) {
            unit = TimeUnit.MILLISECONDS;
        }
        return new MdcThreadPoolExecutor(MDC.getCopyOfContextMap(), corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
    }

    /**
     * Pool where task threads take fixed MDC from the thread that creates the pool.
     */
    @SuppressWarnings("unchecked")
    public static MdcThreadPoolExecutor newWithCurrentMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                          TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return new MdcThreadPoolExecutor(MDC.getCopyOfContextMap(), corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue);
    }

    private MdcThreadPoolExecutor(Map<String, String> fixedContext, int corePoolSize, int maximumPoolSize,
                                  long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.fixedContext = fixedContext;
        this.useFixedContext = (fixedContext != null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }

    /**
     * All executions will have MDC injected. {@code ThreadPoolExecutor}'s submission methods ({@code submit()} etc.)
     * all delegate to this.
     */
    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, getContextForTask()));
        logger.debug("线程池监控：当前ActiveCount {}, workQueue长度 {}", getActiveCount(), getQueue().size());
    }

    @Override
    public Future<?> submit(Runnable command) {
        return super.submit(wrap(command, getContextForTask()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return super.submit(() -> wrap(callable, getContextForTask()));
    }

    public static <T> T wrap(final Callable<T> callable, final Map<String, String> context) throws Exception {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return callable.call();
            } finally {
                runFinally();
            }
    }

    private static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                runFinally();
            }
        };
    }

    private static void runFinally(){
        Map previous = MDC.getCopyOfContextMap();
        if (previous == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(previous);
        }
    }
}

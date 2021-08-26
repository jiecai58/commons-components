package com.util.executor;

import org.apache.log4j.MDC;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.*;


public class ThreadPoolExecutorTest {

    private final static String SESSION_KEY = "sessionId";

    @Test
    public void tokenTest() throws ExecutionException, InterruptedException {
        String token = UUID.randomUUID().toString().replace("-", "");
        MDC.put(SESSION_KEY, token);

        ThreadPoolExecutor executorService = MdcThreadPoolExecutor.ThreadPoolExecutor(1, 5, 10L, TimeUnit.MILLISECONDS);

        //submit
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                System.out.println("submit方法执行任务完成" + "   thread name: " + Thread.currentThread().getName());
                return "";
            }
        });
        System.out.println(future.get());
        //submit lambda表达式
        Future<String> a = executorService.submit(()->{
            System.out.println("   thread namea: " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("submita方法执行任务完成" + "   thread name: " + Thread.currentThread().getName());
            return "a";
        });
        //execute
        executorService.execute(()->{
            System.out.println("   thread nameb: " + Thread.currentThread().getName());
            System.out.println("execute方法执行任务完成" + "   thread name: " + Thread.currentThread().getName());
        });
        
    }
}

package com.zlc.gulu.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig implements WebMvcConfigurer {
    //规定线程池核心线程数：3
    private static final int CORE_POLL_SIZE = 3;
    //规定线程池最大线程数： 5
    private static final int MAX_POOL_SIZE = 5;
    //规定线程池持有的队列容量：100
    private static final int QUEUE_CAPACITY = 100;
    //规定线程池 线程等待时间：1L
    private static final Long KEEP_ALIVE_TIME = 1L;

    @Bean
    @Scope("prototype") // 设置为多例
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                CORE_POLL_SIZE,  // 设置线程池核心线程数
                MAX_POOL_SIZE,   // 设置线程池最大线程数
                KEEP_ALIVE_TIME, // 设置线程等待时间
                TimeUnit.SECONDS,// 设置时间单位
                new ArrayBlockingQueue<>(QUEUE_CAPACITY), // 设置线程池使用ArrayBlockingQueue队列
                new ThreadPoolExecutor.CallerRunsPolicy() // 设置线程池饱和政策:CallerRunsPolicy(),"该策略既不会抛弃任务，也不会抛出异常，而是将任务回推到调用者
        );
    }
}

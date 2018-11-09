package com.zdzc.rmiserver.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2018/8/30 0030.
 */
@Configuration
public class ThreadPoolConfig {

    @Value("${ThreadPool.corePoolSize}")
    private int corePoolSize;

    @Value("${ThreadPool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${ThreadPool.queueCapacity}")
    private int queueCapacity;

    @Value("${ThreadPool.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
//        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
//        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}

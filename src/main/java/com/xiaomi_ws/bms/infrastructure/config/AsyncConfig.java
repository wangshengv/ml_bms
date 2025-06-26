package com.xiaomi_ws.bms.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${bms.warning.async.core-pool-size:10}")
    private int corePoolSize;

    @Value("${bms.warning.async.max-pool-size:50}")
    private int maxPoolSize;

    @Value("${bms.warning.async.queue-capacity:1000}")
    private int queueCapacity;

    @Value("${bms.warning.async.thread-name-prefix:warning-async-}")
    private String threadNamePrefix;

    @Bean(name = "warningTaskExecutor")
    public Executor warningTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}

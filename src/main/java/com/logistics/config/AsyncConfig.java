package com.logistics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration for Async Processing
 * Custom ExecutorService for handling background order processing
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    @Value("${logistics.async.core-pool-size}")
    private int corePoolSize;

    @Value("${logistics.async.max-pool-size}")
    private int maxPoolSize;

    @Value("${logistics.async.queue-capacity}")
    private int queueCapacity;

    @Value("${logistics.async.thread-name-prefix}")
    private String threadNamePrefix;

    @Value("${logistics.async.await-termination-seconds}")
    private int awaitTerminationSeconds;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        logger.info("Initializing custom ThreadPoolTaskExecutor for async processing");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.initialize();

        return executor;
    }
}

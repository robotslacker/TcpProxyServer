package com.robotslacker.tcpproxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author robotslacker
 */
@Configuration
@EnableAsync
public class ThreadPoolsConfig {

    /**
     * 配置线程池中的线程名称前缀
     */
    @Value("${threadPool.threadNamePrefix}")
    private String threadNamePrefix;

    /**
     * 配置线程池中的核心线程数
     */
    @Value("${threadPool.corePoolSize}")
    private Integer corePoolSize;
    /**
     * 配置最大线程数
     */
    @Value("${threadPool.maxPoolSize}")
    private Integer maxPoolSize;

    /**
     * 配置队列大小
     */
    @Value("${threadPool.queueCapacity}")
    private Integer queueCapacity;

    /**
     * 线程池配置
     *
     * @return 异步调用的执行器
     */
    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        // 处理程序遭到拒绝将抛出运行时 RejectedExecutionException
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 等待线程完成操作
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}

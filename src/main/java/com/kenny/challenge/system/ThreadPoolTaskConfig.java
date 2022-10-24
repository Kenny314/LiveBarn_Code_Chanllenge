package com.kenny.challenge.system;

import com.kenny.challenge.service.impl.SushiOrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@PropertySource("classpath:thread.properties")
public class ThreadPoolTaskConfig implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTaskConfig.class);
    @Value("${thread-pool-task.config.corePoolSize:3}")
    private Integer corePoolSize;
    @Value("${thread-pool-task.config.maxPoolSize:3}")
    private Integer maxPoolSize;
    @Value("${thread-pool-task.config.keepAliveTime:60}")
    private Integer keepAliveTime;
    @Value("${thread-pool-task.config.queueCapacity:10}")
    private Integer queueCapacity;
    @Value("${thread-pool-task.config.threadNamePrefix:Chef-}")
    private String threadNamePrefix;
    @Value("${thread-pool-task.config.rejectedExecutionHandler:CallerRunsPolicy}")
    private String rejectedExecutionHandler;


    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveTime);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        threadPoolTaskExecutor.setRejectedExecutionHandler(getRejectedExecutionHandler(rejectedExecutionHandler));
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
    /**
     * 根据传入的参数获取拒绝策略
     * @param rejectedName 拒绝策略名，比如 CallerRunsPolicy
     * @return RejectedExecutionHandler 实例对象，没有匹配的策略时，默认取 CallerRunsPolicy 实例
     */
    public RejectedExecutionHandler getRejectedExecutionHandler(String rejectedName){
        Map<String, RejectedExecutionHandler> rejectedExecutionHandlerMap = new HashMap<>(16);
        rejectedExecutionHandlerMap.put("CallerRunsPolicy", new ThreadPoolExecutor.CallerRunsPolicy());
        rejectedExecutionHandlerMap.put("AbortPolicy", new ThreadPoolExecutor.AbortPolicy());
        rejectedExecutionHandlerMap.put("DiscardPolicy", new ThreadPoolExecutor.DiscardPolicy());
        rejectedExecutionHandlerMap.put("DiscardOldestPolicy", new ThreadPoolExecutor.DiscardOldestPolicy());
        return rejectedExecutionHandlerMap.getOrDefault(rejectedName, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}

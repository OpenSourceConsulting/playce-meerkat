package com.athena.meerkat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class ThreadPoolConfig {
	
	@Value("${thread.core.pool.size:2}")
	private int corePoolSize;
	
	@Value("${thread.max.pool.size:10}")
	private int threadMaxPoolSize;
	
	@Value("${thread.queue.capacity:10}")
	private int queueCapacity;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 * @see com.athena.meerkat.controller.web.monitoring.stat.MonStatisticsAnalyzer
	 */
	@Bean
	public TaskExecutor taskExecutor() {
	    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
	    taskExecutor.setCorePoolSize(corePoolSize);
	    taskExecutor.setMaxPoolSize(threadMaxPoolSize);
	    taskExecutor.setQueueCapacity(queueCapacity);
	    taskExecutor.afterPropertiesSet();
	    return taskExecutor;
	}
    

}
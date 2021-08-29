package com.jetlink.processconfig;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ProcessConfig {
	
	  @Bean(name="taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor exec  =  new ThreadPoolTaskExecutor() ;
		exec.setCorePoolSize(Runtime.getRuntime().availableProcessors());
		exec.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
		exec.setQueueCapacity(100);
		exec.setThreadNamePrefix("userThread");
		exec.initialize();
		return exec ;
	}

}










































































































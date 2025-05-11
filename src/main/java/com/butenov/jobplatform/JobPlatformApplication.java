package com.butenov.jobplatform;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class JobPlatformApplication
{

	public static void main(final String[] args)
	{
		SpringApplication.run(JobPlatformApplication.class, args);
	}

	@Bean(name = "llmExecutor")
	public Executor asyncExecutor()
	{
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(50);
		executor.setThreadNamePrefix("LLM-");
		executor.initialize();
		return executor;
	}

}

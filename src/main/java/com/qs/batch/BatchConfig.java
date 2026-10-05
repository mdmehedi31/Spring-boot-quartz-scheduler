package com.qs.batch;


import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BatchConfig {

    @Bean("asyncJobLauncher")
    @Primary
    public JobOperator asyncJobLauncher(
            JobRepository jobRepository,
            @Qualifier("emailTaskExecutor")
            TaskExecutor executor,
            JobRegistry jobRegistry)
            throws Exception {

        TaskExecutorJobOperator jobOperator = new TaskExecutorJobOperator();
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setTaskExecutor(executor);
        jobOperator.setJobRegistry(jobRegistry);
        return jobOperator;
    }


    @Bean
    @Primary
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean("emailTaskExecutor")
    @Primary
    public TaskExecutor emailTaskExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);

        executor.initialize();

        return executor;
    }

}

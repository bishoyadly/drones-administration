package com.musalasoft.dronesadministration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableJpaAuditing
@EnableScheduling
public class AppConfiguration {

    @Value("${dronesSchedulerPoolSize}")
    private Integer dronesSchedulerPoolSize;

    @Value("${dronesSchedulerName}")
    private String dronesSchedulerName;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadGroupName(dronesSchedulerName);
        scheduler.setPoolSize(dronesSchedulerPoolSize);
        scheduler.initialize();
        return scheduler;
    }
}

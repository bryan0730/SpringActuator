package com.example.actuator.batch.bt01.job;

import com.example.actuator.batch.bt01.listener.HttpTraceListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HttpTraceJobConfig {

    private final HttpTraceListener httpTraceListener;

    public HttpTraceJobConfig(HttpTraceListener httpTraceListener){
        this.httpTraceListener = httpTraceListener;
    }

    @Bean
    public Job httpTraceJob(JobRepository jobRepository, Step httpTraceStep2){
        return new JobBuilder("httpTraceJob", jobRepository)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .listener(httpTraceListener)
                .start(httpTraceStep2)
                .build();
    }
}

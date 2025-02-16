package com.example.actuator.batch.bt01.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class HttpTraceStepConfig2 {

    @Bean
    @JobScope
    public Step httpTraceStep2(JobRepository jobRepository, Tasklet HttpTraceTasklet, PlatformTransactionManager platformTransactionManager
    ,@Value("#{jobParameters[ym]}") String ym){
        log.info("httpTraceStep2 param :: {}", ym);
        return new StepBuilder("httpTraceStep2", jobRepository)
                .tasklet(HttpTraceTasklet, platformTransactionManager)
                .build()
                ;
    }
}

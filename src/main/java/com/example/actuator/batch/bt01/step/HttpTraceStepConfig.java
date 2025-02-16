package com.example.actuator.batch.bt01.step;

import com.example.actuator.batch.bt01.model.HttpTraceAggregate;
import com.example.actuator.batch.bt01.repository.HttpTraceAggregateRepository;
import com.example.actuator.batch.bt01.repository.HttpTraceRepository;
import com.example.actuator.model.HttpTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Configuration
public class HttpTraceStepConfig {

    private final HttpTraceRepository httpTraceRepository;
    private final HttpTraceAggregateRepository httpTraceAggregateRepository;

    public HttpTraceStepConfig(HttpTraceRepository httpTraceRepository, HttpTraceAggregateRepository httpTraceAggregateRepository){
        this.httpTraceRepository = httpTraceRepository;
        this.httpTraceAggregateRepository = httpTraceAggregateRepository;
    }


    @Bean
    @JobScope
    public Step httpTraceStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){

        return new StepBuilder("httpTraceStep", jobRepository)
                .<HttpTrace, HttpTraceAggregate>chunk(500, platformTransactionManager)
                .reader(httpTraceItemReader(null))
                .processor(httpTraceItemProcessor(null))
                .writer(httpTraceItemWriter())
                .allowStartIfComplete(true)
                .build()
                ;
    }

    @Bean
    @StepScope
    public ItemReader<HttpTrace> httpTraceItemReader(@Value("#{jobParameters[ym]}") String ym){
        return new RepositoryItemReaderBuilder<HttpTrace>()
                .name("jpaHttpTraceReader")
                .repository(httpTraceRepository)
                .methodName("findByHttpRequestDateBefore")
                .pageSize(500)
                .arguments(ym)
                .sorts(Collections.singletonMap("http_request_date", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<HttpTrace, HttpTraceAggregate> httpTraceItemProcessor(@Value("#{jobParameters[ym]}") String ym){
        return item -> {
            log.info("itemProcessor job parameter :: {}", ym);
            HttpTraceAggregate aggregate = new HttpTraceAggregate();
            aggregate.setAggregateId(UUID.randomUUID().toString());
            aggregate.setAggregateDate(ym);
            aggregate.setHttpMethod(item.getHttpMethod());
            aggregate.setHttpUri(item.getHttpUri());
            aggregate.setRegDate(LocalDateTime.now());

            // 요청 횟수 증가
            aggregate.setRequestCount(1); // 기본적으로 1회로 시작

            // 응답 상태에 따라 성공/실패 카운트
            if (item.getHttpResponseStatus().startsWith("2")) {
                aggregate.setSuccessCount(1);
            } else if (item.getHttpResponseStatus().startsWith("4") || item.getHttpResponseStatus().startsWith("5")) {
                aggregate.setErrorCount(1);
            }

            return aggregate;
        };
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<HttpTraceAggregate> httpTraceItemWriter(){
        return new RepositoryItemWriterBuilder<HttpTraceAggregate>()
                .repository(httpTraceAggregateRepository)
                .methodName("save")
                .build();
    }




}

package com.example.actuator.batch.bt01.tasklet;

import com.example.actuator.batch.bt01.model.HttpTraceAggregate;
import com.example.actuator.batch.bt01.repository.HttpTraceAggregateRepository;
import com.example.actuator.batch.bt01.repository.HttpTraceRepository;
import com.example.actuator.model.HttpTrace;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class HttpTraceTasklet implements Tasklet, StepExecutionListener {

    @Value("#{jobParameters[date]}")
    private String ym;

    private final HttpTraceRepository httpTraceRepository;

    private final HttpTraceAggregateRepository aggregateRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

//        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
//        String paramYm = jobParameters.getString("ym");

        log.info("HttpTraceTasklet jobParameter :: {}", ym);

        List<HttpTrace> httpTraces = httpTraceRepository.findByHttpRequestDateBefore(ym);
        log.info("HTTP_TRACE LIST SIZE :: {}", httpTraces.size());
        List<HttpTraceAggregate> aggregates = httpTraces.stream().collect(Collectors.groupingBy(
                        trace -> new RequestKey(trace.getHttpUri(), trace.getHttpMethod(), trace.getHttpRequestDate().toLocalDate()), // 일자별로 그룹화
                        Collectors.summarizingInt(trace -> {
                            return trace.getHttpResponseStatus().startsWith("4") || trace.getHttpResponseStatus().startsWith("5") ? 0 : 1;
                        })))
                .entrySet().stream() // Map<Entry<RequestKey, IntSummaryStatistics>>
                .map(entry -> {
                    RequestKey key = entry.getKey();
                    long totalRequests = entry.getValue().getCount();
                    long successRequests = entry.getValue().getSum();
                    long errorRequests = totalRequests - successRequests;

                    return new HttpTraceAggregate(
                            UUID.randomUUID().toString(),
                            LocalDateTime.now(),
                            key.getRequestDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")), // aggregateDate (yyyyMMdd)
                            key.getHttpMethod(),
                            key.getHttpUri(),
                            (int) totalRequests,
                            (int) successRequests,
                            (int) errorRequests
                    );
                })
                .collect(Collectors.toList());

        aggregateRepository.saveAll(aggregates);

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return StepExecutionListener.super.afterStep(stepExecution);
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
    public static class RequestKey {
        private String httpUri;
        private String httpMethod;
        private LocalDate requestDate;
    }
}

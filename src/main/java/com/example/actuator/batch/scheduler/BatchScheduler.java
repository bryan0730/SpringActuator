package com.example.actuator.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BatchScheduler {

    /* --job.name=httpTraceJob ym=20250204 */
    private final JobLauncher jobLauncher;
    private final Job httpTraceJob;


//    @Scheduled(cron = "*/5 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void runJob() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("ym", currentDate)
                .addString("uuid", UUID.randomUUID().toString())
                .toJobParameters();

        try {
            jobLauncher.run(httpTraceJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

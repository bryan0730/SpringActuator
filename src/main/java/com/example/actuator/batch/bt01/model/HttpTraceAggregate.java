package com.example.actuator.batch.bt01.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "http_trace_aggregate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HttpTraceAggregate {

    @Id
    private String aggregateId;
    private LocalDateTime regDate;
    private String aggregateDate; // YYYYMMDD 포맷
    private String httpMethod;
    private String httpUri;
    private int requestCount;
    private int successCount;
    private int errorCount;
}

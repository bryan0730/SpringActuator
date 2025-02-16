package com.example.actuator.batch.bt01.repository;

import com.example.actuator.batch.bt01.model.HttpTraceAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpTraceAggregateRepository extends JpaRepository<HttpTraceAggregate, String> {
}

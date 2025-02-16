package com.example.actuator.repository;

import com.example.actuator.model.HttpTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpExchangeLogRepository extends JpaRepository<HttpTrace, Long> {
}

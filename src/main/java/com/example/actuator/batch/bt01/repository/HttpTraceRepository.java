package com.example.actuator.batch.bt01.repository;

import com.example.actuator.model.HttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HttpTraceRepository extends JpaRepository<HttpTrace, String> {

    @Query(value = "SELECT * FROM http_trace WHERE date_format(http_request_date, '%Y%m%d') = :date", nativeQuery = true)
    Page<HttpTrace> findByHttpRequestDateBefore(@Param("date") String date, Pageable pageable);

    @Query(value = "SELECT * FROM http_trace WHERE date_format(http_request_date, '%Y%m%d') = :date", nativeQuery = true)
    List<HttpTrace> findByHttpRequestDateBefore(@Param("date") String date);

    @Query(value = "SELECT " +
            "        date_format(http_request_date, '%Y%m%d') as http_request_date, http_method, http_uri " +
            "        , count(1) AS REQUEST_COUNT " +
            "        , SUM(IF(substr(http_response_status,1,1)='2',1,0)) AS SUCCESS_COUNT " +
            "        , SUM(IF(substr(http_response_status,1,1)!='2',1,0)) AS ERROR_COUNT " +
            "    FROM " +
            "        http_trace " +
            "    WHERE " +
            "        date_format(http_request_date, '%Y%m%d') = :date " +
            "GROUP BY date_format(http_request_date, '%Y%m%d'), http_method, http_uri", nativeQuery = true)
    List<HttpTrace> findByHttpTraceSum(@Param("date") String date);
}

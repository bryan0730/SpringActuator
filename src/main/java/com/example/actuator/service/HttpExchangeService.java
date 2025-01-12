package com.example.actuator.service;

import com.example.actuator.model.HttpTrace;
import com.example.actuator.repository.HttpExchangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HttpExchangeService {

    private static final Logger log = LoggerFactory.getLogger(HttpExchangeService.class);
    private final HttpExchangeLogRepository httpExchangeLogRepository;

    @Transactional
    public void saveAllToDatabase(List<HttpExchange> exchanges) {
        try {
            List<HttpTrace> httpTraceList = exchanges.stream().map(exchange -> {
                HttpTrace trace = new HttpTrace();
                // 요청 데이터 매핑
                trace.setHttpUri(exchange.getRequest().getUri().toString());
                trace.setHttpMethod(exchange.getRequest().getMethod());
                trace.setHttpRequestDate(
                        LocalDateTime.ofInstant(exchange.getTimestamp(), ZoneId.of("Asia/Seoul")) // 서울 시간대
                );
                trace.setHttpRequestHost(exchange.getRequest().getUri().getHost());
                // 요청 헤더 매핑
                trace.setHttpRequestHeaderContentType(
                        exchange.getRequest().getHeaders()
                                .getOrDefault("Content-Type", List.of("")).get(0) // 기본값 처리
                );
                trace.setHttpRequestHeaderAccept(
                        exchange.getRequest().getHeaders()
                                .getOrDefault("Accept", List.of("")).get(0) // 기본값 처리
                );
                trace.setHttpRequestAuthorization(
                        exchange.getRequest().getHeaders()
                                .getOrDefault("Authorization", List.of("")).get(0) // 기본값 처리
                );
                // 요청 본문 매핑
                // trace.setHttpRequestBody(exchange.getRequest().get);
                // 응답 데이터 매핑
                trace.setHttpResponseStatus(
                        String.valueOf(exchange.getResponse().getStatus())
                );

                trace.setHttpResponseDate(
                        LocalDateTime.ofInstant(exchange.getTimestamp(), ZoneId.of("Asia/Seoul")) // 서울 시간대
                ); // 응답 시간은 요청 시간과 동일
                // trace.setHttpResponseBody(exchange.getResponse().getBody());
                return trace;
            }).collect(Collectors.toList());
            // 데이터베이스에 저장
            httpExchangeLogRepository.saveAll(httpTraceList);
            log.info("Saved all exchanges to database: {}", httpTraceList.toString());
        } catch (Exception e) {
            log.info("Failed to save exchanges to database", e);
            exchanges.clear();
            throw e; // 트랜잭션 롤백 발생
        }
    }
}


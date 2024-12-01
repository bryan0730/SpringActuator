package com.example.actuator.service;

import com.example.actuator.model.HttpRequestLog;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HttpLoggingService {

    private final HttpExchangeRepository httpExchangeRepository;
//    private final LogRepository repository; /* MySql db 저장 Repo */

    @Scheduled(cron = "0 * * * * *")
    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void saveHttpExchanges() {
        List<HttpExchange> exchanges = httpExchangeRepository.findAll();
        for (HttpExchange exchange : exchanges) {
            HttpRequestLog log = new HttpRequestLog();
            log.setMethod(exchange.getRequest().getMethod());
            log.setUri(exchange.getRequest().getUri().toString());
            log.setStatus(exchange.getResponse().getStatus());
            log.setTimestamp(System.currentTimeMillis());
            /* 추가 data 저장 필요 request/response 분리해서?  */

//            repository.save(log); /* mabatis mysql db 저장 */
        }
    }
}

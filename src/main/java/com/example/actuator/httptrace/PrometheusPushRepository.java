package com.example.actuator.httptrace;

import com.example.actuator.service.HttpExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Primary
@Repository
public class PrometheusPushRepository implements HttpExchangeRepository {

    private static final int THRESHOLD = 10; // 푸시 임계값
    private static final Logger log = LoggerFactory.getLogger(PrometheusPushRepository.class);
    private final LinkedList<HttpExchange> exchanges = new LinkedList<>();
    private final PushGateway pushGateway;
    private final Gauge requestGauge;
    private final CollectorRegistry registry;
    private final HttpExchangeService httpExchangeService;
    private final ApplicationContext applicationContext; // ApplicationContext 주입

    public PrometheusPushRepository(HttpExchangeService httpExchangeService,
                                    ApplicationContext applicationContext) {
        this.httpExchangeService = httpExchangeService;
        this.applicationContext = applicationContext; // ApplicationContext 주입
        this.pushGateway = new PushGateway("localhost:9091"); // Push Gateway URL 설정
        this.registry = new CollectorRegistry();
        this.requestGauge = Gauge.build()
                .name("http_requests_guage")
                .help("Total HTTP requests")
                .labelNames("method", "uri", "status")
                .register(registry);
    }

    @Override
    public List<HttpExchange> findAll() {
        log.info("findAll Repository");
        synchronized (exchanges) {
            return new LinkedList<>(exchanges);
        }
    }

    @Override
    @Transactional
    public void add(HttpExchange exchange) {
        synchronized (exchanges) {
            exchanges.add(exchange);
            // Prometheus 메트릭 갱신
            String method = exchange.getRequest().getMethod();
            String uri = exchange.getRequest().getUri().getPath();
            int status = exchange.getResponse().getStatus();
            requestGauge.labels(method, uri, String.valueOf(status)).inc();
            log.info("exchange value " + exchange.toString());

            // 요청이 임계값에 도달하면 푸시
            if (exchanges.size() >= THRESHOLD) {
                httpExchangeService.saveAllToDatabase(exchanges);
                PrometheusPushRepository proxy = applicationContext.getBean(PrometheusPushRepository.class);
                proxy.pushToPrometheus();
            }
        }
    }

    @Transactional
    public void pushToPrometheus() {
        try {
            pushGateway.push(registry, "http_exchange_job");
            log.info("Pushed to Prometheus Push Gateway");
            // 요청 기록 초기화
            exchanges.clear();
        } catch (IOException e) {
            log.info("An IOException occurred during Prometheus push", e);
            e.printStackTrace();
            // rollback
        }
    }
}

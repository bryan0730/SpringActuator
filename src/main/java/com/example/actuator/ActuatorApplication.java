package com.example.actuator;

import com.example.actuator.httptrace.PrometheusPushRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;

@EnableScheduling
@SpringBootApplication
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

//    @Bean
//    @Primary
//    public HttpExchangeRepository httpExchangeRepository(){
//        return new PrometheusPushRepository();
//    }

}

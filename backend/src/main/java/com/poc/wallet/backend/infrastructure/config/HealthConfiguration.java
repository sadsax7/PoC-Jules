package com.poc.wallet.backend.infrastructure.config;

import com.poc.wallet.backend.application.health.HealthCheckService;
import com.poc.wallet.backend.domain.health.HealthCheckPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfiguration {

    @Bean
    public HealthCheckService healthCheckService(HealthCheckPort healthCheckPort) {
        return new HealthCheckService(healthCheckPort);
    }
}

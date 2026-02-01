package com.poc.wallet.backend.application.health;

import com.poc.wallet.backend.domain.health.HealthCheckPort;
import com.poc.wallet.backend.domain.health.HealthStatus;

public class HealthCheckService {
    private final HealthCheckPort healthCheckPort;

    public HealthCheckService(HealthCheckPort healthCheckPort) {
        this.healthCheckPort = healthCheckPort;
    }

    public HealthStatus check() {
        return healthCheckPort.check();
    }
}

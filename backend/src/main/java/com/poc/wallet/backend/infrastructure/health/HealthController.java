package com.poc.wallet.backend.infrastructure.health;

import com.poc.wallet.backend.application.health.HealthCheckService;
import com.poc.wallet.backend.domain.health.HealthStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final HealthCheckService healthCheckService;

    public HealthController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthStatus status = healthCheckService.check();
        HttpStatus httpStatus = status == HealthStatus.UP ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(httpStatus).body(new HealthResponse(status.name()));
    }
}

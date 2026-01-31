package com.poc.wallet.backend.infrastructure.health;

import com.poc.wallet.backend.domain.health.HealthCheckPort;
import com.poc.wallet.backend.domain.health.HealthStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoHealthCheckAdapter implements HealthCheckPort {
    private final MongoTemplate mongoTemplate;

    public MongoHealthCheckAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public HealthStatus check() {
        try {
            mongoTemplate.executeCommand("{ ping: 1 }");
            return HealthStatus.UP;
        } catch (Exception ex) {
            return HealthStatus.DOWN;
        }
    }
}

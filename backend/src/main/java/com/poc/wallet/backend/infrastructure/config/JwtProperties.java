package com.poc.wallet.backend.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpiresMinutes,
        long tempTokenExpiresMinutes
) {
}

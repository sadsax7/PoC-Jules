package com.poc.wallet.backend.domain.user;

import java.util.Optional;

public final class Email {
    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Optional<Email> ofNullable(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(of(raw));
    }

    public static Email of(String raw) {
        String trimmed = raw == null ? null : raw.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        return new Email(trimmed);
    }

    public String value() {
        return value;
    }
}

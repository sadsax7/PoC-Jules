package com.poc.wallet.backend.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public final class PhoneNumber {
    private static final Pattern E164_PATTERN = Pattern.compile("^\\+\\d{8,15}$");

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidPhoneException("Phone number is required");
        }
        String normalized = normalize(raw);
        if (!E164_PATTERN.matcher(normalized).matches()) {
            throw new InvalidPhoneException("Phone number must be in E.164 format");
        }
        return new PhoneNumber(normalized);
    }

    public String value() {
        return value;
    }

    private static String normalize(String raw) {
        return raw.replaceAll("[\\s-]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

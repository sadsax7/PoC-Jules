package com.poc.wallet.backend.domain.user;

public interface PasswordHasherPort {
    String hash(String rawPassword);
}

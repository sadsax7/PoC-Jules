package com.poc.wallet.backend.domain.auth;

public class MfaInvalidCodeException extends RuntimeException {
    public MfaInvalidCodeException(String message) {
        super(message);
    }
}

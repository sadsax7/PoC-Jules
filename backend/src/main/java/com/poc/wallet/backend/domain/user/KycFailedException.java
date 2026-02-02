package com.poc.wallet.backend.domain.user;

public class KycFailedException extends RuntimeException {
    public KycFailedException(String message) {
        super(message);
    }
}

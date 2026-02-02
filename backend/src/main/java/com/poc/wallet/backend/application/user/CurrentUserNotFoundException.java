package com.poc.wallet.backend.application.user;

public class CurrentUserNotFoundException extends RuntimeException {
    public CurrentUserNotFoundException(String message) {
        super(message);
    }
}

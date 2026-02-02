package com.poc.wallet.backend.domain.auth;

public class TokenExpiredException extends InvalidTokenException {
    public TokenExpiredException(String message) {
        super(message);
    }
}

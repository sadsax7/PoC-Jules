package com.poc.wallet.backend.domain.auth;

public interface TokenServicePort {
    String generateAccessToken(String userId, String role);

    String generateTempToken(String userId);

    TokenClaims parseAndValidate(String token, TokenType expectedType);
}

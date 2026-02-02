package com.poc.wallet.backend.domain.auth;

public record TokenClaims(String userId, TokenType tokenType, String role) {
}

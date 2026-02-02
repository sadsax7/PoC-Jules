package com.poc.wallet.backend.application.auth;

public record MfaVerifyCommand(String tempToken, String code) {
}

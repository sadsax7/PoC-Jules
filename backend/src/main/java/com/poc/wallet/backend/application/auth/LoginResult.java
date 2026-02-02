package com.poc.wallet.backend.application.auth;

public record LoginResult(String accessToken, String status, String tempToken) {
    public static LoginResult accessToken(String accessToken) {
        return new LoginResult(accessToken, null, null);
    }

    public static LoginResult mfaRequired(String tempToken) {
        return new LoginResult(null, "MFA_REQUIRED", tempToken);
    }

    public boolean isMfaRequired() {
        return "MFA_REQUIRED".equals(status);
    }
}

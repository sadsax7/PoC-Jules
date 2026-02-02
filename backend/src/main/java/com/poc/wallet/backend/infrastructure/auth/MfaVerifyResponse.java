package com.poc.wallet.backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record MfaVerifyResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken
) {
}

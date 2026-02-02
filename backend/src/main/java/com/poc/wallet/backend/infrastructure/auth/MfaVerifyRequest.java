package com.poc.wallet.backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MfaVerifyRequest(
        @NotBlank
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String tempToken,
        @NotBlank
        @Schema(example = "123456")
        String code
) {
}

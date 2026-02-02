package com.poc.wallet.backend.infrastructure.users;

import com.poc.wallet.backend.domain.user.KycStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record UsersMeResponse(
        @Schema(example = "user-123")
        String userId,
        @Schema(example = "+5491122334455")
        String phone,
        @Schema(example = "user@example.com", nullable = true)
        String email,
        @Schema(example = "PENDING")
        KycStatus kycStatus,
        @Schema(example = "false")
        boolean mfaEnabled,
        @Schema(example = "2026-02-02T12:34:56Z")
        Instant createdAt
) {
}

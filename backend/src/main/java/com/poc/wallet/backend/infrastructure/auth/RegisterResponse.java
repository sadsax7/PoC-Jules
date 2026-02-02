package com.poc.wallet.backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponse(
        @Schema(example = "user-123")
        String userId
) {
}

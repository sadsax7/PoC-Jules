package com.poc.wallet.backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        @Schema(example = "+5491122334455")
        String phone,
        @NotBlank
        @Schema(example = "Pass1234")
        String password
) {
}

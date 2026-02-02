package com.poc.wallet.backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        @Schema(example = "+5491122334455")
        String phone,
        @Email
        @Schema(example = "user@example.com", nullable = true)
        String email,
        @NotBlank
        @Schema(example = "Pass1234")
        String password
) {
}

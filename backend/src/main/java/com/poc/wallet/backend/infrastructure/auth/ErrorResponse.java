package com.poc.wallet.backend.infrastructure.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @Schema(example = "VALIDATION_ERROR")
        String errorCode,
        @Schema(example = "Phone number must be in E.164 format")
        String message,
        @Schema(example = "a1b2c3", nullable = true)
        String traceId
) {
}

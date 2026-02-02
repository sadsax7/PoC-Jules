package com.poc.wallet.backend.application.user;

import com.poc.wallet.backend.domain.user.KycStatus;

import java.time.Instant;

public record GetCurrentUserResult(
        String userId,
        String phone,
        String email,
        KycStatus kycStatus,
        boolean mfaEnabled,
        Instant createdAt
) {
}

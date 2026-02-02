package com.poc.wallet.backend.application.user;

import com.poc.wallet.backend.domain.user.Email;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;

public class GetCurrentUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public GetCurrentUserUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public GetCurrentUserResult getCurrentUser(String userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new CurrentUserNotFoundException("Current user not found"));

        return new GetCurrentUserResult(
                user.id().orElseThrow(() -> new IllegalStateException("User ID is required")),
                user.phone().value(),
                user.email().map(Email::value).orElse(null),
                user.kycStatus(),
                user.mfaEnabled(),
                user.createdAt()
        );
    }
}

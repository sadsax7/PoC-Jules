package com.poc.wallet.backend.application.auth;

import com.poc.wallet.backend.domain.auth.InvalidCredentialsException;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;

import java.util.Optional;

public class LoginUseCase {
    private static final String ROLE_USER = "USER";

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenServicePort tokenServicePort;

    public LoginUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordHasherPort passwordHasherPort,
            TokenServicePort tokenServicePort
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordHasherPort = passwordHasherPort;
        this.tokenServicePort = tokenServicePort;
    }

    public LoginResult login(LoginCommand command) {
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        Optional<User> userOptional = userRepositoryPort.findByPhone(phoneNumber);
        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = userOptional.get();
        if (!passwordHasherPort.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String userId = user.id().orElseThrow(() -> new IllegalStateException("User ID is required"));

        if (user.mfaEnabled()) {
            String tempToken = tokenServicePort.generateTempToken(userId);
            return LoginResult.mfaRequired(tempToken);
        }

        String accessToken = tokenServicePort.generateAccessToken(userId, ROLE_USER);
        return LoginResult.accessToken(accessToken);
    }
}

package com.poc.wallet.backend.application.auth;

import com.poc.wallet.backend.domain.auth.InvalidCredentialsException;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.user.KycStatus;
import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @Mock
    private TokenServicePort tokenServicePort;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        loginUseCase = new LoginUseCase(
                userRepositoryPort,
                passwordHasherPort,
                tokenServicePort,
                false
        );
    }

    @Test
    void loginReturnsAccessTokenWhenMfaDisabled() {
        // Arrange
        LoginCommand command = new LoginCommand("+12345678", "Pass1234");
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        User user = User.rehydrate(
                "user-1",
                phoneNumber,
                null,
                "hashed",
                KycStatus.PENDING,
                false,
                Instant.now()
        );

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches(command.password(), "hashed")).thenReturn(true);
        when(tokenServicePort.generateAccessToken("user-1", "USER")).thenReturn("access-token");

        // Act
        LoginResult result = loginUseCase.login(command);

        // Assert
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.status()).isNull();
        verify(tokenServicePort, never()).generateTempToken(any(String.class));
    }

    @Test
    void loginReturnsMfaRequiredWhenMfaEnabled() {
        // Arrange
        LoginCommand command = new LoginCommand("+12345678", "Pass1234");
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        User user = User.rehydrate(
                "user-2",
                phoneNumber,
                null,
                "hashed",
                KycStatus.PENDING,
                true,
                Instant.now()
        );

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches(command.password(), "hashed")).thenReturn(true);
        when(tokenServicePort.generateTempToken("user-2")).thenReturn("temp-token");

        // Act
        LoginResult result = loginUseCase.login(command);

        // Assert
        assertThat(result.status()).isEqualTo("MFA_REQUIRED");
        assertThat(result.tempToken()).isEqualTo("temp-token");
        verify(tokenServicePort, never()).generateAccessToken(any(String.class), any(String.class));
    }

    @Test
    void loginReturnsMfaRequiredWhenForceMfaEnabled() {
        // Arrange
        LoginCommand command = new LoginCommand("+12345678", "Pass1234");
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        User user = User.rehydrate(
                "user-4",
                phoneNumber,
                null,
                "hashed",
                KycStatus.PENDING,
                false,
                Instant.now()
        );

        loginUseCase = new LoginUseCase(
                userRepositoryPort,
                passwordHasherPort,
                tokenServicePort,
                true
        );

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches(command.password(), "hashed")).thenReturn(true);
        when(tokenServicePort.generateTempToken("user-4")).thenReturn("temp-token");

        // Act
        LoginResult result = loginUseCase.login(command);

        // Assert
        assertThat(result.status()).isEqualTo("MFA_REQUIRED");
        assertThat(result.tempToken()).isEqualTo("temp-token");
        verify(tokenServicePort, never()).generateAccessToken(any(String.class), any(String.class));
    }

    @Test
    void loginInvalidPasswordThrowsInvalidCredentials() {
        // Arrange
        LoginCommand command = new LoginCommand("+12345678", "WrongPass");
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        User user = User.rehydrate(
                "user-3",
                phoneNumber,
                null,
                "hashed",
                KycStatus.PENDING,
                false,
                Instant.now()
        );

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches(command.password(), "hashed")).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> loginUseCase.login(command))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}

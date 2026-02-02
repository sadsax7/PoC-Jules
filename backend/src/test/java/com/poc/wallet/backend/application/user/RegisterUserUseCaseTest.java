package com.poc.wallet.backend.application.user;

import com.poc.wallet.backend.domain.user.Email;
import com.poc.wallet.backend.domain.user.InvalidPasswordException;
import com.poc.wallet.backend.domain.user.KycFailedException;
import com.poc.wallet.backend.domain.user.KycServicePort;
import com.poc.wallet.backend.domain.user.KycStatus;
import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserAlreadyExistsException;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private KycServicePort kycServicePort;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void registerHappyPathReturnsUserId() {
        // Arrange
        RegisterUserCommand command = new RegisterUserCommand(
                "+12345678",
                "user@example.com",
                "Pass1234"
        );
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        Email email = Email.of(command.email());

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(Optional.empty());
        when(kycServicePort.isKycPassed(phoneNumber)).thenReturn(true);
        when(passwordHasherPort.hash(command.password())).thenReturn("hashed");
        when(userRepositoryPort.save(any(User.class))).thenReturn(
                User.rehydrate(
                        "user-123",
                        phoneNumber,
                        email,
                        "hashed",
                        KycStatus.PENDING,
                        false,
                        Instant.now()
                )
        );

        // Act
        RegisterUserResult result = useCase.register(command);

        // Assert
        assertThat(result.userId()).isEqualTo("user-123");
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    void registerDuplicatePhoneThrowsAndSkipsKycAndSave() {
        // Arrange
        RegisterUserCommand command = new RegisterUserCommand(
                "+12345678",
                null,
                "Pass1234"
        );
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(
                Optional.of(User.rehydrate(
                        "existing",
                        phoneNumber,
                        null,
                        "hashed",
                        KycStatus.PENDING,
                        false,
                        Instant.now()
                ))
        );

        // Act + Assert
        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(kycServicePort, never()).isKycPassed(any(PhoneNumber.class));
        verify(passwordHasherPort, never()).hash(any(String.class));
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    void registerKycFailThrowsAndSkipsSave() {
        // Arrange
        RegisterUserCommand command = new RegisterUserCommand(
                "+12345678",
                null,
                "Pass1234"
        );
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());

        when(userRepositoryPort.findByPhone(phoneNumber)).thenReturn(Optional.empty());
        when(kycServicePort.isKycPassed(phoneNumber)).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(KycFailedException.class);

        verify(userRepositoryPort, never()).save(any(User.class));
        verify(passwordHasherPort, never()).hash(any(String.class));
    }

    @Test
    void registerInvalidPasswordThrowsAndSkipsPorts() {
        // Arrange
        RegisterUserCommand command = new RegisterUserCommand(
                "+12345678",
                null,
                "short1"
        );

        // Act + Assert
        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(InvalidPasswordException.class);

        verifyNoInteractions(userRepositoryPort, kycServicePort, passwordHasherPort);
    }
}

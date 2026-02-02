package com.poc.wallet.backend.domain.user;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void createNewSetsDefaultsAndCreatedAt() {
        // Arrange
        PhoneNumber phone = PhoneNumber.of("+12345678");
        Email email = Email.of("user@example.com");
        String passwordHash = "hashed-password";

        // Act
        User user = User.createNew(phone, email, passwordHash);

        // Assert
        assertThat(user.kycStatus()).isEqualTo(KycStatus.PENDING);
        assertThat(user.mfaEnabled()).isFalse();
        assertThat(user.createdAt()).isNotNull();
        assertThat(user.passwordHash()).isEqualTo(passwordHash);
    }

    @Test
    void userDoesNotExposePlainPasswordAccessor() {
        // Arrange
        boolean hasGetPassword = Arrays.stream(User.class.getMethods())
                .anyMatch(method -> method.getName().equals("getPassword"));

        // Act
        boolean hasGetPasswordHash = Arrays.stream(User.class.getMethods())
                .anyMatch(method -> method.getName().equals("passwordHash"));

        // Assert
        assertThat(hasGetPassword).isFalse();
        assertThat(hasGetPasswordHash).isTrue();
    }
}

package com.poc.wallet.backend.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordPolicyTest {

    @Test
    void validateAcceptsPasswordWithLetterAndNumberAndValidLength() {
        // Arrange
        String rawPassword = "Pass1234";

        // Act
        PasswordPolicy.validate(rawPassword);

        // Assert
        assertThat(PasswordPolicy.isValid(rawPassword)).isTrue();
    }

    @Test
    void validateRejectsPasswordsOutsidePolicy() {
        // Arrange
        String tooShort = "Pa1";
        String noNumber = "Password";
        String noLetter = "12345678";
        String tooLong = "A1234567890123456789012345678901234567890123456789012345678901234";

        // Act + Assert
        assertThatThrownBy(() -> PasswordPolicy.validate(tooShort))
                .isInstanceOf(InvalidPasswordException.class);
        assertThatThrownBy(() -> PasswordPolicy.validate(noNumber))
                .isInstanceOf(InvalidPasswordException.class);
        assertThatThrownBy(() -> PasswordPolicy.validate(noLetter))
                .isInstanceOf(InvalidPasswordException.class);
        assertThatThrownBy(() -> PasswordPolicy.validate(tooLong))
                .isInstanceOf(InvalidPasswordException.class);
    }
}

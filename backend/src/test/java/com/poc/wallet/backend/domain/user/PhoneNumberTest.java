package com.poc.wallet.backend.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    void ofNormalizesSpacesAndHyphens() {
        // Arrange
        String raw = "+1 234-567-8901";

        // Act
        PhoneNumber phoneNumber = PhoneNumber.of(raw);

        // Assert
        assertThat(phoneNumber.value()).isEqualTo("+12345678901");
    }

    @Test
    void ofAcceptsValidE164Numbers() {
        // Arrange
        String raw = "+12345678";

        // Act
        PhoneNumber phoneNumber = PhoneNumber.of(raw);

        // Assert
        assertThat(phoneNumber.value()).isEqualTo(raw);
    }

    @Test
    void ofRejectsInvalidNumbers() {
        // Arrange
        String tooShort = "+1234567";
        String tooLong = "+1234567890123456";
        String missingPlus = "12345678";
        String withLetters = "+12345ABCD";

        // Act + Assert
        assertThatThrownBy(() -> PhoneNumber.of(tooShort))
                .isInstanceOf(InvalidPhoneException.class);
        assertThatThrownBy(() -> PhoneNumber.of(tooLong))
                .isInstanceOf(InvalidPhoneException.class);
        assertThatThrownBy(() -> PhoneNumber.of(missingPlus))
                .isInstanceOf(InvalidPhoneException.class);
        assertThatThrownBy(() -> PhoneNumber.of(withLetters))
                .isInstanceOf(InvalidPhoneException.class);
    }
}

package com.poc.wallet.backend.application.auth;

import com.poc.wallet.backend.domain.auth.InvalidTokenException;
import com.poc.wallet.backend.domain.auth.MfaInvalidCodeException;
import com.poc.wallet.backend.domain.auth.TokenClaims;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.auth.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MfaVerifyUseCaseTest {

    @Mock
    private TokenServicePort tokenServicePort;

    @InjectMocks
    private MfaVerifyUseCase mfaVerifyUseCase;

    @Test
    void verifyReturnsAccessTokenWhenValid() {
        // Arrange
        MfaVerifyCommand command = new MfaVerifyCommand("temp-token", "123456");
        when(tokenServicePort.parseAndValidate("temp-token", TokenType.TEMP))
                .thenReturn(new TokenClaims("user-1", TokenType.TEMP, null));
        when(tokenServicePort.generateAccessToken("user-1", "USER"))
                .thenReturn("access-token");

        // Act
        MfaVerifyResult result = mfaVerifyUseCase.verify(command);

        // Assert
        assertThat(result.accessToken()).isEqualTo("access-token");
    }

    @Test
    void verifyInvalidCodeThrows() {
        // Arrange
        MfaVerifyCommand command = new MfaVerifyCommand("temp-token", "000000");

        // Act + Assert
        assertThatThrownBy(() -> mfaVerifyUseCase.verify(command))
                .isInstanceOf(MfaInvalidCodeException.class);
    }

    @Test
    void verifyInvalidTokenThrows() {
        // Arrange
        MfaVerifyCommand command = new MfaVerifyCommand("bad-token", "123456");
        when(tokenServicePort.parseAndValidate("bad-token", TokenType.TEMP))
                .thenThrow(new InvalidTokenException("Invalid token"));

        // Act + Assert
        assertThatThrownBy(() -> mfaVerifyUseCase.verify(command))
                .isInstanceOf(InvalidTokenException.class);
    }
}

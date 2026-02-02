package com.poc.wallet.backend.application.user;

import com.poc.wallet.backend.domain.user.Email;
import com.poc.wallet.backend.domain.user.KycStatus;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCurrentUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetCurrentUserUseCase useCase;

    @Test
    void getCurrentUserReturnsSafeDto() {
        // Arrange
        String userId = "user-123";
        Instant createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        User user = User.rehydrate(
                userId,
                PhoneNumber.of("+12345678"),
                Email.of("user@example.com"),
                "hashed",
                KycStatus.PENDING,
                false,
                createdAt
        );

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));

        // Act
        GetCurrentUserResult result = useCase.getCurrentUser(userId);

        // Assert
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.phone()).isEqualTo("+12345678");
        assertThat(result.email()).isEqualTo("user@example.com");
        assertThat(result.kycStatus()).isEqualTo(KycStatus.PENDING);
        assertThat(result.mfaEnabled()).isFalse();
        assertThat(result.createdAt()).isEqualTo(createdAt);

        boolean hasPasswordHashField = false;
        for (var component : GetCurrentUserResult.class.getRecordComponents()) {
            if (component.getName().equals("passwordHash")) {
                hasPasswordHashField = true;
                break;
            }
        }
        assertThat(hasPasswordHashField).isFalse();
    }

    @Test
    void getCurrentUserThrowsWhenMissing() {
        // Arrange
        String userId = "missing-id";
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> useCase.getCurrentUser(userId))
                .isInstanceOf(CurrentUserNotFoundException.class);
    }
}

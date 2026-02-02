package com.poc.wallet.backend.infrastructure.user;

import com.poc.wallet.backend.domain.user.Email;
import com.poc.wallet.backend.domain.user.KycStatus;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest(properties = "spring.data.mongodb.auto-index-creation=true")
@Testcontainers
class MongoUserRepositoryAdapterIT {

    @Container
    static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
    }

    @Autowired
    private SpringDataUserRepository repository;

    @Test
    void saveAndFindByPhoneReturnsUser() {
        // Arrange
        MongoUserRepositoryAdapter adapter = new MongoUserRepositoryAdapter(repository);
        PhoneNumber phone = PhoneNumber.of("+12345678");
        User user = User.rehydrate(
                null,
                phone,
                Email.of("user@example.com"),
                "hashed",
                KycStatus.PENDING,
                false,
                Instant.now()
        );

        // Act
        User saved = adapter.save(user);
        User found = adapter.findByPhone(phone).orElseThrow();

        // Assert
        assertThat(saved.id()).isPresent();
        assertThat(found.phone().value()).isEqualTo("+12345678");
        assertThat(found.email().orElseThrow().value()).isEqualTo("user@example.com");
        assertThat(found.passwordHash()).isEqualTo("hashed");
    }

    @Test
    void duplicatePhoneViolatesUniqueIndex() {
        // Arrange
        MongoUserRepositoryAdapter adapter = new MongoUserRepositoryAdapter(repository);
        PhoneNumber phone = PhoneNumber.of("+12345678");
        User first = User.rehydrate(
                null,
                phone,
                null,
                "hash-1",
                KycStatus.PENDING,
                false,
                Instant.now()
        );
        User second = User.rehydrate(
                null,
                phone,
                null,
                "hash-2",
                KycStatus.PENDING,
                false,
                Instant.now()
        );

        // Act
        adapter.save(first);

        // Assert
        assertThatThrownBy(() -> adapter.save(second))
                .isInstanceOfAny(DuplicateKeyException.class, DataIntegrityViolationException.class);
    }

    @Test
    void findByIdReturnsUserWhenExists() {
        // Arrange
        MongoUserRepositoryAdapter adapter = new MongoUserRepositoryAdapter(repository);
        PhoneNumber phone = PhoneNumber.of("+12345678");
        Instant createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        User user = User.rehydrate(
                null,
                phone,
                Email.of("user@example.com"),
                "hashed",
                KycStatus.PENDING,
                false,
                createdAt
        );

        // Act
        User saved = adapter.save(user);
        User found = adapter.findById(saved.id().orElseThrow()).orElseThrow();

        // Assert
        assertThat(found.phone().value()).isEqualTo("+12345678");
        assertThat(found.email().orElseThrow().value()).isEqualTo("user@example.com");
        assertThat(found.kycStatus()).isEqualTo(KycStatus.PENDING);
        assertThat(found.mfaEnabled()).isFalse();
        assertThat(found.createdAt()).isEqualTo(createdAt);
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        // Arrange
        MongoUserRepositoryAdapter adapter = new MongoUserRepositoryAdapter(repository);

        // Act
        var result = adapter.findById("id-inexistente");

        // Assert
        assertThat(result).isEmpty();
    }
}

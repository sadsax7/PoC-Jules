package com.poc.wallet.backend.infrastructure.users;

import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.user.KycStatus;
import com.poc.wallet.backend.infrastructure.user.SpringDataUserRepository;
import com.poc.wallet.backend.infrastructure.user.UserDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UsersControllerTest {

    @Container
    static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
        registry.add("app.jwt.secret", () -> "0123456789abcdef0123456789abcdef");
        registry.add("app.jwt.access-token-expires-minutes", () -> "20");
        registry.add("app.jwt.temp-token-expires-minutes", () -> "5");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataUserRepository repository;

    @Autowired
    private TokenServicePort tokenServicePort;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getMeReturns200WithAccessToken() throws Exception {
        // Arrange
        Instant createdAt = Instant.parse("2026-02-02T12:34:56Z");
        UserDocument saved = repository.save(new UserDocument(
                null,
                "+5491122334455",
                "user@example.com",
                "hashed",
                KycStatus.PENDING,
                false,
                createdAt
        ));
        String userId = saved.getId();
        assertThat(userId).isNotNull();
        String accessToken = tokenServicePort.generateAccessToken(userId, "USER");

        // Act + Assert
        mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.phone").value("+5491122334455"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.kycStatus").value("PENDING"))
                .andExpect(jsonPath("$.mfaEnabled").value(false))
                .andExpect(jsonPath("$.createdAt").value("2026-02-02T12:34:56Z"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void getMeReturns401WhenMissingToken() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMeReturns401WhenTokenInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer abc"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMeReturns401WhenUserNotFound() throws Exception {
        // Arrange
        String accessToken = tokenServicePort.generateAccessToken("missing-user", "USER");

        // Act + Assert
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("CURRENT_USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Current user not found"));
    }
}

package com.poc.wallet.backend.infrastructure.config;

import com.poc.wallet.backend.application.user.RegisterUserUseCase;
import com.poc.wallet.backend.domain.user.KycServicePort;
import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;
import com.poc.wallet.backend.infrastructure.user.BCryptPasswordHasherAdapter;
import com.poc.wallet.backend.infrastructure.user.MockKycServiceAdapter;
import com.poc.wallet.backend.infrastructure.user.MongoUserRepositoryAdapter;
import com.poc.wallet.backend.infrastructure.user.SpringDataUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegistrationConfiguration {

    @Bean
    public UserRepositoryPort userRepositoryPort(SpringDataUserRepository repository) {
        return new MongoUserRepositoryAdapter(repository);
    }

    @Bean
    public KycServicePort kycServicePort() {
        return new MockKycServiceAdapter();
    }

    @Bean
    public PasswordHasherPort passwordHasherPort() {
        return new BCryptPasswordHasherAdapter();
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepositoryPort userRepositoryPort,
            KycServicePort kycServicePort,
            PasswordHasherPort passwordHasherPort
    ) {
        return new RegisterUserUseCase(userRepositoryPort, kycServicePort, passwordHasherPort);
    }
}

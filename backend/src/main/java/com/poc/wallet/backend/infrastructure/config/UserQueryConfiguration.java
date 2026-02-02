package com.poc.wallet.backend.infrastructure.config;

import com.poc.wallet.backend.application.user.GetCurrentUserUseCase;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserQueryConfiguration {

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new GetCurrentUserUseCase(userRepositoryPort);
    }
}

package com.poc.wallet.backend.infrastructure.config;

import com.poc.wallet.backend.application.auth.LoginUseCase;
import com.poc.wallet.backend.application.auth.MfaVerifyUseCase;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;
import com.poc.wallet.backend.infrastructure.security.JwtTokenServiceAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class AuthConfiguration {

    @Bean
    public TokenServicePort tokenServicePort(JwtProperties jwtProperties) {
        return new JwtTokenServiceAdapter(jwtProperties);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordHasherPort passwordHasherPort,
            TokenServicePort tokenServicePort
    ) {
        return new LoginUseCase(userRepositoryPort, passwordHasherPort, tokenServicePort);
    }

    @Bean
    public MfaVerifyUseCase mfaVerifyUseCase(TokenServicePort tokenServicePort) {
        return new MfaVerifyUseCase(tokenServicePort);
    }
}

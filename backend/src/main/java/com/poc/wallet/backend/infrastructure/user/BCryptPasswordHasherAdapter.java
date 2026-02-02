package com.poc.wallet.backend.infrastructure.user;

import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasherAdapter implements PasswordHasherPort {
    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordHasherAdapter() {
        this(new BCryptPasswordEncoder());
    }

    public BCryptPasswordHasherAdapter(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}

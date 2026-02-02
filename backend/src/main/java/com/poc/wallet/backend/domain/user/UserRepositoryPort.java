package com.poc.wallet.backend.domain.user;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByPhone(PhoneNumber phoneNumber);

    User save(User user);
}

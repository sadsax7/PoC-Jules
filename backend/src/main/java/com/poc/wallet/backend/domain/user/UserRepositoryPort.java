package com.poc.wallet.backend.domain.user;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByPhone(PhoneNumber phoneNumber);
    Optional<User> findById(String userId);

    User save(User user);

}

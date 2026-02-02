package com.poc.wallet.backend.infrastructure.user;

import com.poc.wallet.backend.domain.user.Email;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;

import java.util.Optional;

public class MongoUserRepositoryAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository repository;

    public MongoUserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByPhone(PhoneNumber phoneNumber) {
        return repository.findByPhone(phoneNumber.value()).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserDocument saved = repository.save(toDocument(user));
        return toDomain(saved);
    }

    private UserDocument toDocument(User user) {
        return new UserDocument(
                user.id().orElse(null),
                user.phone().value(),
                user.email().map(Email::value).orElse(null),
                user.passwordHash(),
                user.kycStatus(),
                user.mfaEnabled(),
                user.createdAt()
        );
    }

    private User toDomain(UserDocument document) {
        return User.rehydrate(
                document.getId(),
                PhoneNumber.of(document.getPhone()),
                Email.ofNullable(document.getEmail()).orElse(null),
                document.getPasswordHash(),
                document.getKycStatus(),
                document.isMfaEnabled(),
                document.getCreatedAt()
        );
    }
}

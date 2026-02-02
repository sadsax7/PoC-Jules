package com.poc.wallet.backend.application.user;

import com.poc.wallet.backend.domain.user.Email;
import com.poc.wallet.backend.domain.user.KycFailedException;
import com.poc.wallet.backend.domain.user.KycServicePort;
import com.poc.wallet.backend.domain.user.PasswordHasherPort;
import com.poc.wallet.backend.domain.user.PasswordPolicy;
import com.poc.wallet.backend.domain.user.PhoneNumber;
import com.poc.wallet.backend.domain.user.User;
import com.poc.wallet.backend.domain.user.UserAlreadyExistsException;
import com.poc.wallet.backend.domain.user.UserRepositoryPort;

import java.util.Optional;

public class RegisterUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final KycServicePort kycServicePort;
    private final PasswordHasherPort passwordHasherPort;

    public RegisterUserUseCase(
            UserRepositoryPort userRepositoryPort,
            KycServicePort kycServicePort,
            PasswordHasherPort passwordHasherPort
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.kycServicePort = kycServicePort;
        this.passwordHasherPort = passwordHasherPort;
    }

    public RegisterUserResult register(RegisterUserCommand command) {
        PhoneNumber phoneNumber = PhoneNumber.of(command.phone());
        PasswordPolicy.validate(command.password());

        Optional<User> existingUser = userRepositoryPort.findByPhone(phoneNumber);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists for phone");
        }

        if (!kycServicePort.isKycPassed(phoneNumber)) {
            throw new KycFailedException("KYC failed");
        }

        String passwordHash = passwordHasherPort.hash(command.password());
        Email email = Email.ofNullable(command.email()).orElse(null);
        User newUser = User.createNew(phoneNumber, email, passwordHash);
        User saved = userRepositoryPort.save(newUser);

        return new RegisterUserResult(
                saved.id().orElseThrow(() -> new IllegalStateException("User ID is required"))
        );
    }
}

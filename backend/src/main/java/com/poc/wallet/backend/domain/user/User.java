package com.poc.wallet.backend.domain.user;

import java.time.Instant;
import java.util.Optional;

public final class User {
    private final String id;
    private final PhoneNumber phone;
    private final Email email;
    private final String passwordHash;
    private final KycStatus kycStatus;
    private final boolean mfaEnabled;
    private final Instant createdAt;

    private User(
            String id,
            PhoneNumber phone,
            Email email,
            String passwordHash,
            KycStatus kycStatus,
            boolean mfaEnabled,
            Instant createdAt
    ) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new InvalidPasswordException("Password hash is required");
        }
        if (kycStatus == null) {
            throw new IllegalArgumentException("KYC status is required");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt is required");
        }
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.kycStatus = kycStatus;
        this.mfaEnabled = mfaEnabled;
        this.createdAt = createdAt;
    }

    public static User createNew(PhoneNumber phone, Email email, String passwordHash) {
        return new User(
                null,
                phone,
                email,
                passwordHash,
                KycStatus.PENDING,
                false,
                Instant.now()
        );
    }

    public static User rehydrate(
            String id,
            PhoneNumber phone,
            Email email,
            String passwordHash,
            KycStatus kycStatus,
            boolean mfaEnabled,
            Instant createdAt
    ) {
        return new User(id, phone, email, passwordHash, kycStatus, mfaEnabled, createdAt);
    }

    public Optional<String> id() {
        return Optional.ofNullable(id);
    }

    public PhoneNumber phone() {
        return phone;
    }

    public Optional<Email> email() {
        return Optional.ofNullable(email);
    }

    public String passwordHash() {
        return passwordHash;
    }

    public KycStatus kycStatus() {
        return kycStatus;
    }

    public boolean mfaEnabled() {
        return mfaEnabled;
    }

    public Instant createdAt() {
        return createdAt;
    }
}

package com.poc.wallet.backend.infrastructure.user;

import com.poc.wallet.backend.domain.user.KycStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private String phone;

    private String email;

    private String passwordHash;

    private KycStatus kycStatus;

    private boolean mfaEnabled;

    private Instant createdAt;

    public UserDocument() {
    }

    public UserDocument(
            String id,
            String phone,
            String email,
            String passwordHash,
            KycStatus kycStatus,
            boolean mfaEnabled,
            Instant createdAt
    ) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.kycStatus = kycStatus;
        this.mfaEnabled = mfaEnabled;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

package com.poc.wallet.backend.domain.user;

public interface KycServicePort {
    boolean isKycPassed(PhoneNumber phoneNumber);
}

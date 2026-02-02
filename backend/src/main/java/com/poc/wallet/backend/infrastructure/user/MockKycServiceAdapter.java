package com.poc.wallet.backend.infrastructure.user;

import com.poc.wallet.backend.domain.user.KycServicePort;
import com.poc.wallet.backend.domain.user.PhoneNumber;

public class MockKycServiceAdapter implements KycServicePort {
    @Override
    public boolean isKycPassed(PhoneNumber phoneNumber) {
        return !phoneNumber.value().startsWith("+999");
    }
}

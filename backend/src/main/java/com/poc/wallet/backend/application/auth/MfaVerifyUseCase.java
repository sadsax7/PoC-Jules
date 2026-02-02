package com.poc.wallet.backend.application.auth;

import com.poc.wallet.backend.domain.auth.MfaInvalidCodeException;
import com.poc.wallet.backend.domain.auth.TokenClaims;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.auth.TokenType;

public class MfaVerifyUseCase {
    private static final String VALID_CODE = "123456";
    private static final String ROLE_USER = "USER";

    private final TokenServicePort tokenServicePort;

    public MfaVerifyUseCase(TokenServicePort tokenServicePort) {
        this.tokenServicePort = tokenServicePort;
    }

    public MfaVerifyResult verify(MfaVerifyCommand command) {
        if (!VALID_CODE.equals(command.code())) {
            throw new MfaInvalidCodeException("Invalid MFA code");
        }

        TokenClaims claims = tokenServicePort.parseAndValidate(command.tempToken(), TokenType.TEMP);
        String accessToken = tokenServicePort.generateAccessToken(claims.userId(), ROLE_USER);
        return new MfaVerifyResult(accessToken);
    }
}

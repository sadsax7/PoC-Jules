package com.poc.wallet.backend.domain.user;

public final class PasswordPolicy {
    private PasswordPolicy() {
    }

    public static void validate(String rawPassword) {
        if (!isValid(rawPassword)) {
            throw new InvalidPasswordException("Password does not meet policy");
        }
    }

    public static boolean isValid(String rawPassword) {
        if (rawPassword == null) {
            return false;
        }
        int length = rawPassword.length();
        if (length < 8 || length > 64) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i < rawPassword.length(); i++) {
            char c = rawPassword.charAt(i);
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                return true;
            }
        }
        return false;
    }
}

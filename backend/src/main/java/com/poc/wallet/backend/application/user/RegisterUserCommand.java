package com.poc.wallet.backend.application.user;

public record RegisterUserCommand(String phone, String email, String password) {
}

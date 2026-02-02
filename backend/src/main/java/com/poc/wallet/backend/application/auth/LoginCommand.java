package com.poc.wallet.backend.application.auth;

public record LoginCommand(String phone, String password) {
}

package com.poc.wallet.backend.infrastructure.users;

import com.poc.wallet.backend.application.user.CurrentUserNotFoundException;
import com.poc.wallet.backend.infrastructure.auth.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UsersExceptionHandler {

    @ExceptionHandler(CurrentUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCurrentUserNotFound(CurrentUserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("CURRENT_USER_NOT_FOUND", "Current user not found", null));
    }
}

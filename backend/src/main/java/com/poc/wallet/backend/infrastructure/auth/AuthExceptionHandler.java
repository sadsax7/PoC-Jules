package com.poc.wallet.backend.infrastructure.auth;

import com.poc.wallet.backend.domain.user.InvalidPasswordException;
import com.poc.wallet.backend.domain.user.InvalidPhoneException;
import com.poc.wallet.backend.domain.user.KycFailedException;
import com.poc.wallet.backend.domain.user.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("USER_ALREADY_EXISTS", "Phone already registered", null));
    }

    @ExceptionHandler(KycFailedException.class)
    public ResponseEntity<ErrorResponse> handleKycFailed(KycFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("KYC_FAILED", "KYC validation failed", null));
    }

    @ExceptionHandler({InvalidPhoneException.class, InvalidPasswordException.class})
    public ResponseEntity<ErrorResponse> handleDomainValidation(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRequestValidation(MethodArgumentNotValidException ex) {
        String message = "Validation error";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", message, null));
    }
}

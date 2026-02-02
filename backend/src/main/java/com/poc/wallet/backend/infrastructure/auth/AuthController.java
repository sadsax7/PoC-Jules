package com.poc.wallet.backend.infrastructure.auth;

import com.poc.wallet.backend.application.auth.LoginCommand;
import com.poc.wallet.backend.application.auth.LoginResult;
import com.poc.wallet.backend.application.auth.LoginUseCase;
import com.poc.wallet.backend.application.auth.MfaVerifyCommand;
import com.poc.wallet.backend.application.auth.MfaVerifyResult;
import com.poc.wallet.backend.application.auth.MfaVerifyUseCase;
import com.poc.wallet.backend.application.user.RegisterUserCommand;
import com.poc.wallet.backend.application.user.RegisterUserResult;
import com.poc.wallet.backend.application.user.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final MfaVerifyUseCase mfaVerifyUseCase;

    public AuthController(
            RegisterUserUseCase registerUserUseCase,
            LoginUseCase loginUseCase,
            MfaVerifyUseCase mfaVerifyUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
        this.mfaVerifyUseCase = mfaVerifyUseCase;
    }

    @Operation(summary = "Register user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserResult result = registerUserUseCase.register(
                new RegisterUserCommand(request.phone(), request.email(), request.password())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(result.userId()));
    }

    @Operation(summary = "Login user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = loginUseCase.login(new LoginCommand(request.phone(), request.password()));
        if (result.isMfaRequired()) {
            return ResponseEntity.ok(new LoginResponse(null, result.status(), result.tempToken()));
        }
        return ResponseEntity.ok(new LoginResponse(result.accessToken(), null, null));
    }

    @Operation(summary = "Verify MFA code")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "MFA verified",
                    content = @Content(schema = @Schema(implementation = MfaVerifyResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/mfa/verify")
    public ResponseEntity<MfaVerifyResponse> verifyMfa(@Valid @RequestBody MfaVerifyRequest request) {
        MfaVerifyResult result = mfaVerifyUseCase.verify(new MfaVerifyCommand(request.tempToken(), request.code()));
        return ResponseEntity.ok(new MfaVerifyResponse(result.accessToken()));
    }
}

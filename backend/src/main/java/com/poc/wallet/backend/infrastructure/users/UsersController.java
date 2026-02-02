package com.poc.wallet.backend.infrastructure.users;

import com.poc.wallet.backend.application.user.GetCurrentUserResult;
import com.poc.wallet.backend.application.user.GetCurrentUserUseCase;
import com.poc.wallet.backend.application.user.CurrentUserNotFoundException;
import com.poc.wallet.backend.infrastructure.auth.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public UsersController(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @Operation(summary = "Get current user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current user",
                    content = @Content(schema = @Schema(implementation = UsersMeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/me")
    public ResponseEntity<UsersMeResponse> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new CurrentUserNotFoundException("Current user not found");
        }
        GetCurrentUserResult result = getCurrentUserUseCase.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(new UsersMeResponse(
                result.userId(),
                result.phone(),
                result.email(),
                result.kycStatus(),
                result.mfaEnabled(),
                result.createdAt()
        ));
    }
}

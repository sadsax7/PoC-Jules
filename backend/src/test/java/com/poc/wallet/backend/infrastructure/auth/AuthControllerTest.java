package com.poc.wallet.backend.infrastructure.auth;

import com.poc.wallet.backend.application.user.RegisterUserCommand;
import com.poc.wallet.backend.application.user.RegisterUserResult;
import com.poc.wallet.backend.application.user.RegisterUserUseCase;
import com.poc.wallet.backend.domain.user.InvalidPasswordException;
import com.poc.wallet.backend.domain.user.InvalidPhoneException;
import com.poc.wallet.backend.domain.user.KycFailedException;
import com.poc.wallet.backend.domain.user.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void registerReturns201OnHappyPath() throws Exception {
        // Arrange
        when(registerUserUseCase.register(any(RegisterUserCommand.class)))
                .thenReturn(new RegisterUserResult("user-123"));

        // Act + Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+5491122334455\",\"email\":\"user@example.com\",\"password\":\"Pass1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user-123"));
    }

    @Test
    void registerReturns409WhenUserAlreadyExists() throws Exception {
        // Arrange
        when(registerUserUseCase.register(any(RegisterUserCommand.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        // Act + Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+5491122334455\",\"password\":\"Pass1234\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Phone already registered"));
    }

    @Test
    void registerReturns400OnInvalidPhone() throws Exception {
        // Arrange
        when(registerUserUseCase.register(any(RegisterUserCommand.class)))
                .thenThrow(new InvalidPhoneException("Phone number must be in E.164 format"));

        // Act + Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"123\",\"password\":\"Pass1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Phone number must be in E.164 format"));
    }

    @Test
    void registerReturns400OnInvalidPassword() throws Exception {
        // Arrange
        when(registerUserUseCase.register(any(RegisterUserCommand.class)))
                .thenThrow(new InvalidPasswordException("Password does not meet policy"));

        // Act + Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+5491122334455\",\"password\":\"short1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Password does not meet policy"));
    }

    @Test
    void registerReturns400OnKycFail() throws Exception {
        // Arrange
        when(registerUserUseCase.register(any(RegisterUserCommand.class)))
                .thenThrow(new KycFailedException("KYC failed"));

        // Act + Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+99911223344\",\"password\":\"Pass1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("KYC_FAILED"))
                .andExpect(jsonPath("$.message").value("KYC validation failed"));
    }
}

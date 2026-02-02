package com.poc.wallet.backend.infrastructure.security;

import com.poc.wallet.backend.domain.auth.InvalidTokenException;
import com.poc.wallet.backend.domain.auth.TokenClaims;
import com.poc.wallet.backend.domain.auth.TokenExpiredException;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.auth.TokenType;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private TokenServicePort tokenServicePort;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void noAuthorizationHeaderContinuesWithoutAuthentication() throws Exception {
        // Arrange
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenServicePort);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void validTokenAuthenticatesAndContinues() throws Exception {
        // Arrange
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenServicePort);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer valid-token");

        when(tokenServicePort.parseAndValidate("valid-token", TokenType.ACCESS))
                .thenReturn(new TokenClaims("user-123", TokenType.ACCESS, "USER"));

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo("user-123");
        assertThat(authentication.getAuthorities()).extracting("authority")
                .containsExactly("ROLE_USER");
    }

    @Test
    void invalidTokenReturns401AndStopsChain() throws Exception {
        // Arrange
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenServicePort);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer bad-token");

        when(tokenServicePort.parseAndValidate(anyString(), eq(TokenType.ACCESS)))
                .thenThrow(new InvalidTokenException("Invalid token"));

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void expiredTokenReturns401AndStopsChain() throws Exception {
        // Arrange
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenServicePort);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer expired-token");

        when(tokenServicePort.parseAndValidate("expired-token", TokenType.ACCESS))
                .thenThrow(new TokenExpiredException("Token expired"));

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}

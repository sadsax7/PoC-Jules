package com.poc.wallet.backend.infrastructure.security;

import com.poc.wallet.backend.domain.auth.InvalidTokenException;
import com.poc.wallet.backend.domain.auth.TokenClaims;
import com.poc.wallet.backend.domain.auth.TokenExpiredException;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.auth.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenServicePort tokenServicePort;

    public JwtAuthenticationFilter(TokenServicePort tokenServicePort) {
        this.tokenServicePort = tokenServicePort;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        try {
            TokenClaims claims = tokenServicePort.parseAndValidate(token, TokenType.ACCESS);
            List<GrantedAuthority> authorities = mapAuthorities(claims.role());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(claims.userId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private List<GrantedAuthority> mapAuthorities(String role) {
        if (role == null || role.isBlank()) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
}

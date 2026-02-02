package com.poc.wallet.backend.infrastructure.security;

import com.poc.wallet.backend.domain.auth.InvalidTokenException;
import com.poc.wallet.backend.domain.auth.TokenClaims;
import com.poc.wallet.backend.domain.auth.TokenExpiredException;
import com.poc.wallet.backend.domain.auth.TokenServicePort;
import com.poc.wallet.backend.domain.auth.TokenType;
import com.poc.wallet.backend.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtTokenServiceAdapter implements TokenServicePort {
    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ROLE_CLAIM = "role";

    private final String secret;
    private final Duration accessTokenTtl;
    private final Duration tempTokenTtl;

    public JwtTokenServiceAdapter(JwtProperties properties) {
        this.secret = properties.secret();
        this.accessTokenTtl = Duration.ofMinutes(properties.accessTokenExpiresMinutes());
        this.tempTokenTtl = Duration.ofMinutes(properties.tempTokenExpiresMinutes());
    }

    @Override
    public String generateAccessToken(String userId, String role) {
        return buildToken(userId, role, TokenType.ACCESS, accessTokenTtl);
    }

    @Override
    public String generateTempToken(String userId) {
        return buildToken(userId, null, TokenType.TEMP, tempTokenTtl);
    }

    @Override
    public TokenClaims parseAndValidate(String token, TokenType expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenTypeRaw = claims.get(TOKEN_TYPE_CLAIM, String.class);
            if (tokenTypeRaw == null) {
                throw new InvalidTokenException("Invalid token");
            }

            TokenType tokenType = TokenType.valueOf(tokenTypeRaw);
            if (tokenType != expectedType) {
                throw new InvalidTokenException("Invalid token type");
            }

            String userId = claims.getSubject();
            if (userId == null || userId.isBlank()) {
                throw new InvalidTokenException("Invalid token");
            }

            String role = claims.get(ROLE_CLAIM, String.class);
            return new TokenClaims(userId, tokenType, role);
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("Token expired");
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    private String buildToken(String userId, String role, TokenType tokenType, Duration ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .claim(TOKEN_TYPE_CLAIM, tokenType.name())
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey signingKey() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret is required");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

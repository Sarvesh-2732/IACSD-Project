package com.swasth.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    private static final String TOKEN_TYPE = "Bearer ";
    private Key signingKey;

    // Initialize signing key once during bean creation
    @PostConstruct
    public void init() {
        // Use at least 256 bits (32 bytes) for HS256
        if (secretKey.length() < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 bytes for HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(int userId, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return getClaims(extractTokenFromBearer(token)).getSubject();
    }

    public String extractUserRole(String token) {
        return getClaims(extractTokenFromBearer(token)).get("role", String.class);
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new SecurityException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, String userId) {
        try {
            String tokenWithoutBearer = extractTokenFromBearer(token);
            return userId.equals(extractUserId(tokenWithoutBearer)) 
                   && !isTokenExpired(tokenWithoutBearer);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private String extractTokenFromBearer(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_TYPE)) {
            return bearerToken.substring(TOKEN_TYPE.length());
        }
        return bearerToken;
    }
}
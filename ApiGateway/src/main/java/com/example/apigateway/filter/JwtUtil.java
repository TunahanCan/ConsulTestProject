package com.example.apigateway.filter;

import com.example.apigateway.exception.JwtValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
@RefreshScope
public class JwtUtil {

    @Value("${auth.app.jwtSecretKey}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        String encodedSecretKey = encodeSecretKey(secretKey);
        log.info("Encoded secretKey: " + encodedSecretKey);
        key = Keys.hmacShaKeyFor(encodedSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeSecretKey(String key) {
        return Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return isTokenExpired(token);
    }

    public void validateJwtToken(String authToken) {
        try {
            getAllClaimsFromToken(authToken); // Claims bilgisi gerekliyse burada işlenebilir
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new JwtValidationException("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtValidationException("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new JwtValidationException("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new JwtValidationException("JWT token is unsupported", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtValidationException("JWT claims string is empty", e);
        }
    }
}
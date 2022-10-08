package com.example.authservice.service;


import com.example.authservice.commonhandler.exception.ApiException;
import com.example.authservice.domain.payload.AuthResponse;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUserDetailService;
import com.example.authservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RefreshScope
@Slf4j
public class RefreshTokenService {
    @Value("${auth.app.jwtExpirationMs}")
    private String expirationTime;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtUserDetailService jwtUserDetailService;


    public AuthResponse tokenRefresh(Claims refreshTokenClaims) throws Exception {
        return jwtUserDetailService.updateJwtTokenProvider(refreshTokenClaims);
    }

    public Claims verifyRefreshToken(String refreshToken, String userName) {
        Claims refreshTokenClaims = null;
        try {
            refreshTokenClaims = jwtUtil.getAllClaimsFromToken(refreshToken);
            if (!(jwtUtil.getUserNameFromClaims(refreshTokenClaims).equals(userName))) {
                log.error("Token verify exception username not match ");
                throw new ApiException("Token verify exception username not match", HttpStatus.FORBIDDEN);
            }
            return refreshTokenClaims;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
    }


}
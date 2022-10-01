package com.example.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RefreshScope
@Slf4j
public class JwtUtil {

    @Value("${auth.app.jwtSecretKey}")
    private String secretKey;

    @Value("${auth.app.jwtExpirationMs}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init() {
        secretKey =  Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        System.out.println("secretkey-> " +secretKey);
        key =  Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateAccesToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUsername());
        claims.put("Roles", user.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
        long expirationTimeLong = Long.parseLong(expirationTime) * 3;
        String token = doGenerateToken(claims, user.getUsername(), expirationTimeLong);
        return token;
    }

    public String generateRefreshToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUsername());
        claims.put("Roles", Arrays.asList("ROLE_REFRESH_TOKEN"));
        long expirationTimeLong = Long.parseLong(expirationTime) * 15;
        String token = doGenerateToken(claims, user.getUsername(), expirationTimeLong);
        return token;
    }

    private String doGenerateToken(Map<String, Object> claims, String username, long expirationTimeLong) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();

    }

    public String getUserNameFromClaims(Claims claims) {
        return getClaimItem(claims, Claims::getSubject);
    }

    private <T> T getClaimItem(Claims claims, Function<Claims, T> claimResolver) {
        return claimResolver.apply(claims);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }


}
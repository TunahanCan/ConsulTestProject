package com.example.schoolservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RefreshScope
public class JwtUtil {

    @Value("${auth.jwtSecretKey}")
    private String secretKey;

    private static Key key;

    @PostConstruct
    public void init() {
        // Secret key'i Base64 ile encode ediyoruz
        String encodedSecretKey = encodeSecretKey(secretKey);
        log.info("Encoded secretKey: " + encodedSecretKey);
        key = Keys.hmacShaKeyFor(encodedSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Base64 ile secretKey'i encode eden yardımcı metot
    private String encodeSecretKey(String key) {
        return Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public List<String> getAuthoritiesFromClaims(Claims claims) {
        List<String> roleList
                = new ArrayList<>(Arrays.asList(claims.get("Roles").toString().split(",")));
        roleList = roleList.stream().map( itr-> itr.substring(1,itr.length() - 1))
                .collect(Collectors.toList());
        return  roleList;
    }
}
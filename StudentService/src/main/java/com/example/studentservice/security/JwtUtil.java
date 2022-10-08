package com.example.studentservice.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    @Value("${auth.app.jwtSecretKey}")
    private String secretKey;

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

    public Date getExpirationDate(Claims claims) {
        return claims.getExpiration();
    }


    private Boolean isTokenExpired(Claims claims) {
        final Date expiration = getExpirationDate(claims);
        return expiration.before(new Date());
    }

    public String getUserNameFromClaims(Claims claims) {
        return getClaimItem(claims, Claims::getSubject);
    }

    private <T> T getClaimItem(Claims claims, Function<Claims, T> claimResolver) {
        return claimResolver.apply(claims);
    }

    public  List<String>  getAutauthoritiesFromClaims(Claims claims){
        List<String> roleList
                = new ArrayList<>(Arrays.asList(claims.get("Roles").toString().split(",")));
        roleList = roleList.stream().map( itr-> itr.substring(1,itr.length() - 1))
                .collect(Collectors.toList());
        return  roleList;
    }
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

}
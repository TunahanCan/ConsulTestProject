package com.example.studentservice.security;


import com.example.studentservice.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;



@Slf4j
@Configuration
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final int BEGIN_INDEX = 7;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresRoles loginRequired = handlerMethod.getMethod().getAnnotation(RequiresRoles.class);
        Claims claims = null;
        String token = null;

        if (loginRequired == null) {
            return true;
        }
        final String header = getAuthHeader(request);

        if (header.startsWith("Bearer ")){
            token = header.substring(BEGIN_INDEX);
        }
        else {
            log.warn("Jwt token does not start with Bearer - "+ request.getServletPath());
            throw new ApiException("Jwt token does not start with Bearer - ", HttpStatus.UNAUTHORIZED);
        }

        if (token.isEmpty())
            throw new ApiException("Token is missing in request", HttpStatus.UNAUTHORIZED);
        else {
            try {
                claims = checkToken(token);
            } catch (Exception ex) {
                throw new ApiException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }

        if ( claims == null ) return  false;
        roleControl(claims , Arrays.asList(loginRequired.hasRoles()));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private  boolean roleControl(Claims claims , List<String> mustRoles  ){
        List<String> tokenRoles = jwtUtil.getAutauthoritiesFromClaims(claims);
        if(tokenRoles.isEmpty()) return  true;
        else {
            if( mustRoles.isEmpty() ) return true;
            if( !mustRoles.stream().anyMatch(tokenRoles::contains) )
                throw new ApiException("Forbidden User" , HttpStatus.FORBIDDEN);
            else return true;
        }
    }


    private Claims checkToken(String token) {
        try {
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            return claims;
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


    private String getAuthHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if( header == null || header.isEmpty() )
            throw new ApiException(AUTHORIZATION + " is required", HttpStatus.UNAUTHORIZED);
        return header;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        log.warn("---method executed---");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        log.warn("---Request Completed---");
    }


}
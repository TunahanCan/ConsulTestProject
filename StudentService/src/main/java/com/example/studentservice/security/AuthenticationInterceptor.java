package com.example.studentservice.security;


import com.example.studentservice.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final int TOKEN_PREFIX_LENGTH = 7;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true; // Eğer handler bir HandlerMethod değilse, işlemi geç
        }
        RequiresRoles loginRequired = handlerMethod.getMethod().getAnnotation(RequiresRoles.class);
        if (loginRequired == null) {
            return true; // Yetki kontrolü gerekmiyorsa işlemi geç
        }

        String token = extractTokenFromRequest(request);
        Claims claims = validateToken(token);

        roleControl(claims, Arrays.asList(loginRequired.hasRoles()));

        return true;
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or doesn't start with Bearer");
            throw new ApiException("Invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        return header.substring(TOKEN_PREFIX_LENGTH);
    }

    private Claims validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new ApiException("Token is missing in request", HttpStatus.UNAUTHORIZED);
        }

        try {
            return jwtUtil.getAllClaimsFromToken(token);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            log.error("JWT validation error: {}", e.getMessage());
            throw new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private void roleControl(Claims claims, List<String> requiredRoles) {
        List<String> tokenRoles = jwtUtil.getAutauthoritiesFromClaims(claims);

        if (!requiredRoles.isEmpty() && tokenRoles.stream().noneMatch(requiredRoles::contains)) {
            throw new ApiException("User does not have required roles", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        log.info("Method executed: {}", request.getServletPath());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        log.info("Request completed: {}", request.getServletPath());
        if (ex != null) {
            log.error("Error occurred after request completion", ex);
        }
    }
}

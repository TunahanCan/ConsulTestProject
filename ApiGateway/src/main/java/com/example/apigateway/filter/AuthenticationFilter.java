package com.example.apigateway.filter;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@Order(1)
@AllArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtUtil jwtUtil;

    private static final String AUTHORIZATION = "Authorization";
    private static final String ID_HEADER = "id";
    private static final String ROLE_HEADER = "role";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (isAuthMissing(request)) {
                return onError(exchange, "Authorization header is missing in request");
            }

            final String token = getAuthHeader(request);
            try {
                jwtUtil.validateJwtToken(token);
            } catch (Exception ex) {
                return onError(exchange, ex.getMessage());
            }

            populateRequestWithHeaders(exchange, token);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorResponse = convertToJson(err);
        byte[] bytes = errorResponse.getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    private String convertToJson(String err) {
        return String.format("{\"error\": \"%s\"}", err);
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(AUTHORIZATION).getFirst().replace("Bearer ", "");
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AUTHORIZATION);
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header(ID_HEADER, String.valueOf(claims.get("id")))
                .header(ROLE_HEADER, String.valueOf(claims.get("role")))
                .build();
    }
}

package com.example.apigateway;

import com.example.apigateway.filter.RouterValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;



public class RouterValidatorTest {
    private final RouterValidator routerValidator = new RouterValidator();

    @Test
    public void testIsSecuredWithOpenApiEndpoint() {
        ServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.GET, "/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .build();

        boolean isSecured = routerValidator.isSecured.test(request);
        Assertions.assertThat(isSecured).isFalse();
    }

    @Test
    public void testIsSecuredWithSecuredEndpoint() {
        ServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.GET, "/student/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .build();

        boolean isSecured = routerValidator.isSecured.test(request);
        Assertions.assertThat(isSecured).isTrue();
    }
}

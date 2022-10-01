package com.example.apigateway.config;


import com.example.apigateway.filter.AuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GatewayConfig {

    private AuthenticationFilter filter;

    /**
     * @param builder
     * @return */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("student-service", r -> r.path("/student/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://student-service"))

                .route("school-service", r -> r.path("/school/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://school-service"))

                .route("test-service", r -> r.path("/test/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://test-service"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .build();
    }

}

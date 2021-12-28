package com.github.levphilippov.msscbrewerygateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local-discovery")
public class LocalRoutesConfig {
    @Bean
    public RouteLocator beerServiceRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                //beer-service
                .route(r->r.path("/api/v1/beer*", "/api/v1/beer/*", "/api/v1/beer/upc/*")
                        .uri("http://localhost:8080"))
                //beer-order-service
                .route(r->r.path("/api/v1/customers*",
                                "/api/v1/customers/**")
//                                "/api/v1/customers/*/orders",
//                                "/api/v1/customers/*/orders/*",
//                                "/api/v1/customers/*/orders/*/pickup")
                        .uri("http://localhost:8081"))
                .route(r-> r.path("/api/v1/beer/*/inventory*")
                        .uri("http://localhost:8082"))
                .build();
    }
}

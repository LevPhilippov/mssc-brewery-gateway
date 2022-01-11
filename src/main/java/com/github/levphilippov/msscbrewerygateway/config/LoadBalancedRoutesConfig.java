package com.github.levphilippov.msscbrewerygateway.config;

import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local-discovery")
public class LoadBalancedRoutesConfig {

    private TokenRelayGatewayFilterFactory filterFactory;

    public LoadBalancedRoutesConfig(TokenRelayGatewayFilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    @Bean
    @Profile("oauth")
    public RouteLocator beerServiceRouteLocatorWithOAuth2Filter(RouteLocatorBuilder builder) {
        return builder.routes()
                //beer-service
                .route(r->r.path("/api/v1/beer*", "/api/v1/beer/*", "/api/v1/beer/upc/*")
                        .filters(f -> f.filters(filterFactory.apply()).removeRequestHeader("Cookie"))
                        .uri("lb://beer-service"))
                //beer-order-service
                .route(r->r.path("/api/v1/customers*",
                                "/api/v1/customers/**")
                        .uri("lb://beer-order-service"))
                .route(r-> r.path("/api/v1/beer/*/inventory*")
                        .filters(f->f.circuitBreaker(c-> c.setName("inventoryCB")
                                .setFallbackUri("forward:/inventory-failover")
                                .setRouteId("inv-failover")
                                ))
                        .uri("lb://inventory-service"))
                .route(r-> r.path("/inventory-failover*")
                        .uri("lb://inventory-failover-service"))
                .build();
    }

    @Bean
    @Profile("!oauth")
    public RouteLocator beerServiceRouteLocatorUnsecured(RouteLocatorBuilder builder) {
        return builder.routes()
                //beer-service
                .route(r->r.path("/api/v1/beer*", "/api/v1/beer/*", "/api/v1/beer/upc/*")
                        .uri("lb://beer-service"))
                //beer-order-service
                .route(r->r.path("/api/v1/customers*",
                                "/api/v1/customers/**")
                        .uri("lb://beer-order-service"))
                .route(r-> r.path("/api/v1/beer/*/inventory*")
                        .filters(f->f.circuitBreaker(c-> c.setName("inventoryCB")
                                .setFallbackUri("forward:/inventory-failover")
                                .setRouteId("inv-failover")
                                ))
                        .uri("lb://inventory-service"))
                .route(r-> r.path("/inventory-failover*")
                        .uri("lb://inventory-failover-service"))
                .build();
    }
}

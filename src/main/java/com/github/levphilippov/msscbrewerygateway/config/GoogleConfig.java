package com.github.levphilippov.msscbrewerygateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


//@Configuration
//@Profile("google")
public class GoogleConfig {

    //Just a sample for testing routing

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/googlesearch")
                        .filters(f -> f.rewritePath("/googlesearch(?<segment>/?.*)", "/${segment}"))
                        .uri("https://google.com"))
                .build();
    }

}

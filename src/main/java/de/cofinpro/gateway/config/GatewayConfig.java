package de.cofinpro.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    RouteLocator gateway(RouteLocatorBuilder rlb) {
        return rlb
                .routes()
                .route(rs -> rs
                        .path("/recipe/**")
                        .filters(f -> f.prefixPath("/api").tokenRelay())
                        .uri("http://localhost:8080/"))
                .route(rs -> rs
                        .path("/hello/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("http://localhost:8081/"))
                .build();
    }
}

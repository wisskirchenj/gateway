package de.cofinpro.gateway.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("gateway.routes")
@Getter
@Setter
public class GatewayConfig {

    private String recipe;
    private String code;
    private String quiz;

    @Bean
    RouteLocator gateway(RouteLocatorBuilder rlb) {
        return rlb
                .routes()
                .route(rs -> rs
                        .path("/recipe/**")
                        .filters(f -> f.prefixPath("/api").tokenRelay())
                        .uri(recipe))
                .route(rs -> rs
                        .path("/code/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri(code))
                .route(rs -> rs
                        .path("/quiz/**")
                        .filters(f -> f.prefixPath("/juergen/api").tokenRelay())
                        .uri(quiz))
                .build();
    }
}

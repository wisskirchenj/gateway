package de.cofinpro.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.prefixPath;
import static org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions.tokenRelay;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * Configuration class setting up all routes to the code endpoints
 */
@Configuration
@ConfigurationProperties("gateway.routes")
@Getter
@Setter
public class GatewayConfig {

    private String recipe;
    private String code;
    private String quiz;

    @Bean
    RouterFunction<ServerResponse> gateway() {
        return route()
                .GET("/recipe/**", http(recipe))
                .before(prefixPath("/api"))
                .filter(tokenRelay())
                .build()
                .and(
                        route()
                                .GET("/code/**", http(code))
                                .filter(tokenRelay())
                                .build())
                .and(
                        route()
                                .GET("/quiz/**", http(quiz))
                                .before(prefixPath("/juergen/api"))
                                .filter(tokenRelay())
                                .build());
    }
}

package de.wisskirchenj.gateway.service;

import de.wisskirchenj.gateway.GatewayApplication;
import org.springframework.boot.SpringApplication;

public class TestGatewayApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(GatewayApplication::main)
                .with(PostgresTestConfiguration.class)
                .run(args);
    }
}

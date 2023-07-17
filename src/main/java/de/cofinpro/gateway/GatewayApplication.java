package de.cofinpro.gateway;

import de.cofinpro.gateway.aot.HibernateProxyRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@EnableConfigurationProperties
@ImportRuntimeHints({HibernateProxyRegistrar.class})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

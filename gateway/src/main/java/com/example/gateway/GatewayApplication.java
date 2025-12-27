package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/clients/**").uri("lb://SERVICE-CLIENT"))
            .route(r -> r.path("/client/*").uri("lb://SERVICE-CLIENT"))
            .route(r -> r.path("/voitures/**").uri("lb://SERVICE-VOITURE"))
            .route(r -> r.path("/voiture/*").uri("lb://SERVICE-VOITURE"))
            .build();
    }

    /**
     * Version alternative avec routage dynamique automatique
     * Pour utiliser cette version, remplacer le bean routes() ci-dessus par :
     * 
     * @Bean
     * DiscoveryClientRouteDefinitionLocator routesDynamique(
     *         ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp) {
     *     return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
     * }
     * 
     * Accès par nom de service:
     * - http://localhost:8888/SERVICE-CLIENT/clients
     * - http://localhost:8888/SERVICE-VOITURE/voitures
     */
}


package com.medilabo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())  // Désactiver CSRF pour les APIs JWT
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/login", "/login", "/actuator/**").permitAll()  // Autoriser l'accès public à /login
                        .pathMatchers("/patients/**").authenticated()
                        .anyExchange().authenticated()  // Toutes les autres requêtes nécessitent une authentification
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}

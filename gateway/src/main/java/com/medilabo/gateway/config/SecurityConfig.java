package com.medilabo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private String jwtKey = "CM3ipo1PpptXL9db3PpkiSCPI1sZKt5BWZp8ylYYrLf7pORRTjO4MnS+KY09P3TR";

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

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(jwtKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

}

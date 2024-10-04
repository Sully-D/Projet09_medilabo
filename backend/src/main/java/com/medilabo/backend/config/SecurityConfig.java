package com.medilabo.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean JwtDecoder qui sera utilisé pour décoder et valider les tokens JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        // Utiliser l'URL de la clé publique (exposée par le service security)
        String jwkSetUri = "http://localhost:8085/.well-known/jwks.json";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Désactiver CSRF car nous utilisons JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Pas de session, JWT est stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/patients/**").authenticated()  // Protéger l'accès aux patients
                        .anyRequest().permitAll()  // Autoriser toutes les autres requêtes
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));  // Utiliser JWT pour la vérification du token
        return http.build();
    }
}

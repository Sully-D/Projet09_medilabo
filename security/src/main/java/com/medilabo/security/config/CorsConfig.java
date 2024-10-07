package com.medilabo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");  // Autoriser l'origine du gateway
        configuration.addAllowedMethod("*");  // Autoriser toutes les méthodes HTTP
        configuration.addAllowedHeader("*");  // Autoriser tous les en-têtes
        configuration.setAllowCredentials(true);  // Permettre le partage des cookies (JSESSIONID)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Appliquer CORS à toutes les routes
        return source;
    }
}

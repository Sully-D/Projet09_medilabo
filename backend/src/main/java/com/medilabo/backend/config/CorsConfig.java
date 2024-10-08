package com.medilabo.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS) configuration.
 * Defines CORS settings to allow requests from http://localhost:8080, all HTTP methods, all headers,
 * and enables sharing of cookies (JSESSIONID). Applies CORS configuration to all routes.
 */
@Configuration
public class CorsConfig {

    /**
     * Defines CORS settings to allow requests from http://localhost:8080, all HTTP methods, all headers,
     * and enables sharing of cookies (JSESSIONID). Applies CORS configuration to all routes.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // Enable cookie sharing (JSESSIONID)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply CORS to all routes
        return source;
    }
}


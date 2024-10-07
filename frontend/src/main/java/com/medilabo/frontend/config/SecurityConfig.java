package com.medilabo.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    private String jwtKey = "CM3ipo1PpptXL9db3PpkiSCPI1sZKt5BWZp8ylYYrLf7pORRTjO4MnS+KY09P3TR";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // Désactiver CSRF pour les APIs
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/public/**").permitAll()
                    .requestMatchers("/auth/login").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                    .loginPage("/auth/login")
                    .defaultSuccessUrl("/patients", true)
                    .permitAll()  // Permettre l'accès à la page de login
            )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(jwtKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}

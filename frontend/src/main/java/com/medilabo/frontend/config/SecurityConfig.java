package com.medilabo.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
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
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(withDefaults())
            );
        return http.build();
    }
}

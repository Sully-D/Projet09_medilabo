package com.medilabo.security.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;


@Configuration
public class SpringSecurityConfig {

    private String jwtKey = "CM3ipo1PpptXL9db3PpkiSCPI1sZKt5BWZp8ylYYrLf7pORRTjO4MnS+KY09P3TR";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Désactiver CSRF pour les APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // JWT est stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/login").permitAll()  // Autoriser GET sur /login pour afficher la page de login
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()  // Autoriser POST sur /login pour la soumission du formulaire de login
                        .requestMatchers("/favicon.ico", "/css/**", "/js/**").permitAll()  // Permettre l'accès aux ressources statiques
                        .anyRequest().authenticated()  // Protéger toutes les autres routes
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // URL de la page de login personnalisée
                        .defaultSuccessUrl("/patients", true)  // Redirection après succès de connexion
                        .permitAll()  // Permettre l'accès à la page de login
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))  // JWT pour les autres requêtes
                .build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length,"RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder().username("user").password(passwordEncoder().encode("password")).roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
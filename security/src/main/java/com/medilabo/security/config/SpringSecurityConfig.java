package com.medilabo.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;


/**
 * Configuration class for Spring Security settings.
 * Defines security configurations for the application, including custom login page, authentication success handler,
 * user details service, and password encoder.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Defines the security filter chain configuration for HTTP requests.
     * Disables CSRF protection, permits access to specific request matchers like '/auth/**', '/login', and '/error',
     * requires authentication for any other request, sets up a custom login page at '/auth/login',
     * specifies the URL for submitting the login form, and uses a custom authentication success handler.
     *
     * @param http the HttpSecurity object to configure the security filter chain
     * @return the configured SecurityFilterChain object
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**", "/login", "/error").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/auth/login") // Login page
                        .loginProcessingUrl("/login")  // URL used to submit the login form
                        .successHandler(authenticationSuccessHandler())  // Using the custom handler
                        .permitAll()
                )
                .build();
    }

    /**
     * Defines a custom authentication success handler for redirecting users to the gateway URL upon successful authentication.
     *
     * @return an AuthenticationSuccessHandler object that redirects to 'http://192.168.1.87:8080/patients'
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                // Redirect to the gateway URL
                response.sendRedirect("http://localhost:8080/patients");
            }
        };
    }

    /**
     * Creates a UserDetails object for a user with username 'user', encoded password 'user', and role 'USER'.
     * Returns an InMemoryUserDetailsManager containing the created user details.
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Returns a new BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package com.medilabo.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Charger l'utilisateur depuis la base de données ou toute autre source
        // Pour simplifier, créons un utilisateur en mémoire
        if ("user".equals(username)) {
            return org.springframework.security.core.userdetails.User
                    .withUsername("user")
                    .password("$2a$10$DowmVjxIeWdZw4A5BX8ouu8RQ2T.B3lm7I3mWxE5CqgQm5w4eUibC") // mot de passe crypté pour "password"
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }
    }
}

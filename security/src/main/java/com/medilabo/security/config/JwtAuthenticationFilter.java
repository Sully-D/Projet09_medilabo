package com.medilabo.security.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // Utilisation d'une clé générée de manière sécurisée (HS512)
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

    // Durée de validité du token (exemple : 10 heures)
    private final long EXPIRATION_TIME = 36000000; // 10 heures en millisecondes

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Récupération du nom d'utilisateur
        String username = ((UserDetails) authResult.getPrincipal()).getUsername();

        // Génération du token JWT
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Date d'expiration
                .signWith(SECRET_KEY) // Utilisation de la clé secrète sécurisée, l'algorithme est automatiquement déterminé par la clé
                .compact();

        // Ajout du token à l'en-tête de la réponse
        response.addHeader("Authorization", "Bearer " + token);
    }
}

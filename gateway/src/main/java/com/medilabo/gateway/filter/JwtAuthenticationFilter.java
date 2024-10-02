package com.medilabo.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;


@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    // Utiliser une clé sécurisée générée pour la signature JWT (HS512 par exemple)
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("mySecretKeyMySecretKeyMySecretKeyMySecretKey".getBytes()); // Longueur suffisante pour HS512

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Récupérer le header Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si le token est manquant ou mal formé, renvoyer une erreur 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extraire le token JWT
        String token = authHeader.substring(7);

        try {
            // Valider le token avec la méthode recommandée par JJWT (parserBuilder)
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // Configurer la clé secrète pour valider le JWT
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Si le token est valide, ajouter des informations dans les headers et continuer
            exchange.getRequest().mutate().header("x-user-id", claims.getSubject()).build();

        } catch (SignatureException | MalformedJwtException e) {
            // En cas de token invalide, renvoyer une erreur 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Continuer la chaîne de filtres si le token est valide
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;  // Priorité haute pour ce filtre
    }
}

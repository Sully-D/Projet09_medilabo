package com.medilabo.gateway.config;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Récupérer le token depuis l'en-tête Authorization
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || !token.startsWith("Bearer ")) {
                return chain.filter(exchange); // Si pas de token, continuer sans vérification
            }

            token = token.substring(7); // Supprimer "Bearer "

            try {
                // Vérifier et parser le token JWT
                Claims claims = Jwts.parser()
                        .setSigningKey("secretKey") // Clé de signature
                        .parseClaimsJws(token)
                        .getBody();

                // Ajouter des informations à la requête si nécessaire
                exchange.getRequest().mutate().header("user-id", claims.getSubject()).build();
            } catch (JwtException e) {
                return chain.filter(exchange); // Si erreur, continuer sans vérification
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration personnalisée pour le filtre si nécessaire
    }
}


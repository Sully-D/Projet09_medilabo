package com.medilabo.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CookieGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Récupérer le cookie JSESSIONID de la requête
        HttpCookie sessionCookie = exchange.getRequest().getCookies().getFirst("JSESSIONID");

        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            System.out.println("JSESSIONID trouvé : " + sessionId);

            // Ajouter le cookie dans la réponse
            exchange.getResponse().getHeaders().add("Set-Cookie", "JSESSIONID=" + sessionId);

            // Propager le cookie dans la requête
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("Cookie", "JSESSIONID=" + sessionId)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        // Si pas de cookie, continuer sans modification
        return chain.filter(exchange);
    }
}


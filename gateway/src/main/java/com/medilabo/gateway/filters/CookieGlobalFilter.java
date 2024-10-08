package com.medilabo.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter to add and forward the JSESSIONID cookie to all backend services.
 * This allows the backend services to share the same session.
 */
@Component
public class CookieGlobalFilter implements GlobalFilter {

    /**
     * Adds the JSESSIONID cookie to all incoming requests and forwards it to all backend services.
     * This allows the backend services to share the same session.
     * @param exchange the current server web exchange
     * @param chain the gateway filter chain
     * @return a Mono that filters the web exchange
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // get cookie
        HttpCookie sessionCookie = exchange.getRequest().getCookies().getFirst("JSESSIONID");

        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            System.out.println("JSESSIONID found : " + sessionId);

            // add cookie to response
            exchange.getResponse().getHeaders().add("Set-Cookie", "JSESSIONID=" + sessionId);

            // add cookie to request
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("Cookie", "JSESSIONID=" + sessionId)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        // no cookie, do nothing
        return chain.filter(exchange);
    }
}


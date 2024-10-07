package com.medilabo.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SameSite {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");  // Permet de partager les cookies entre plusieurs domaines
        serializer.setUseSecureCookie(true);  // Nécessaire si tu utilises HTTPS
        return serializer;
    }

}

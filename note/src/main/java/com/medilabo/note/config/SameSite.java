package com.medilabo.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Configuration class for setting up the cookie serializer with specific properties.
 * The serializer is configured to have 'SameSite' attribute set to 'None' for sharing cookies across multiple domains.
 * It also enforces the use of secure cookies when HTTPS is used.
 *
 * @return the configured cookie serializer
 */
@Configuration
public class SameSite {

    /**
     * Returns a CookieSerializer object that can be used to customize cookie serialization for session management.
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");  // Share cookies between multiple domains
        serializer.setUseSecureCookie(true);
        return serializer;
    }

}

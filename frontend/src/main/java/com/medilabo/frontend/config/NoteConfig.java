package com.medilabo.frontend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Configuration class for managing properties related to the note service.
 * Uses the prefix "note.service" for configuration properties.
 */
@Component
@ConfigurationProperties(prefix = "note.service")
public class NoteConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

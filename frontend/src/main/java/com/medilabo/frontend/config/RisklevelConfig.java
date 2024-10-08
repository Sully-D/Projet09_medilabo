package com.medilabo.frontend.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Configuration class for managing risk level service properties.
 * This class is annotated as a component and binds external configuration properties
 * with the prefix "risklevel.service" to its fields.
 */
@Component
@ConfigurationProperties(prefix = "risklevel.service")
public class RisklevelConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

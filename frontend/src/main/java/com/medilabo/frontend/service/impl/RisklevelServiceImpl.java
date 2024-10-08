package com.medilabo.frontend.service.impl;

import com.medilabo.frontend.config.RisklevelConfig;
import com.medilabo.frontend.service.RisklevelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Service implementation for interacting with the risk level service.
 * Retrieves the risk level for a specific patient identified by their ID.
 * Utilizes WebClient for making HTTP requests to the risk level service.
 */
@Service
public class RisklevelServiceImpl implements RisklevelService {

    @Autowired
    private WebClient webClient;

    private RisklevelConfig risklevelConfig;

    @Autowired
    private HttpServletRequest request;  // Inject HttpServletRequest to access cookies

    @Autowired
    public RisklevelServiceImpl(RisklevelConfig risklevelConfig) {
        this.risklevelConfig = risklevelConfig;
    }


    /**
     * Retrieves the risk level for a specific patient identified by their ID.
     *
     * @param patientId the ID of the patient
     * @return the risk level for the patient
     */
    @Override
    public String getRiskLevelForPatient(String patientId) {

        String sessionId = getSessionIdFromCookies();   // Retrieve session ID from cookies

        return webClient.get()
                .uri(risklevelConfig.getBaseUrl() + "?patientId=" + patientId)
                .cookie("JSESSIONID", sessionId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Retrieves the session ID from the cookies in the HTTP request.
     * If the cookie is not found, returns null.
     *
     * @return the session ID or null
     */
    private String getSessionIdFromCookies() {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

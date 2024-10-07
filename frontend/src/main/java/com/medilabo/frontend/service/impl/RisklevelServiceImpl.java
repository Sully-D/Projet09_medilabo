package com.medilabo.frontend.service.impl;

import com.medilabo.frontend.config.RisklevelConfig;
import com.medilabo.frontend.service.RisklevelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class RisklevelServiceImpl implements RisklevelService {

    @Autowired
    private WebClient webClient;

    private RisklevelConfig risklevelConfig;

    @Autowired
    private HttpServletRequest request;  // Injection de HttpServletRequest pour accéder aux cookies

    @Autowired
    public RisklevelServiceImpl(RisklevelConfig risklevelConfig) {
        this.risklevelConfig = risklevelConfig;
    }


    @Override
    public String getRiskLevelForPatient(String patientId) {
        // Récupérer le cookie JSESSIONID de la requête
        String sessionId = getSessionIdFromCookies();

        return webClient.get()
                .uri(risklevelConfig.getBaseUrl() + "?patientId=" + patientId)
                .cookie("JSESSIONID", sessionId)  // Transmet le cookie de session
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // Méthode pour récupérer le JSESSIONID depuis les cookies
    private String getSessionIdFromCookies() {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    return cookie.getValue();  // Récupérer la valeur du cookie JSESSIONID
                }
            }
        }
        return null;  // Retourne null si le cookie n'est pas trouvé
    }
}

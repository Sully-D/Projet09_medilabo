package com.medilabo.frontend.service.impl;

import com.medilabo.frontend.config.RisklevelConfig;
import com.medilabo.frontend.service.RisklevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class RisklevelServiceImpl implements RisklevelService {

    @Autowired
    private WebClient webClient;

    private RisklevelConfig risklevelConfig;

    @Autowired
    public RisklevelServiceImpl(RisklevelConfig risklevelConfig) {
        this.risklevelConfig = risklevelConfig;
    }


    @Override
    public String getRiskLevelForPatient(String patientId) {
        return webClient.get()
                .uri(risklevelConfig.getBaseUrl() + "?patientId=" + patientId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

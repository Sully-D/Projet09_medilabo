package com.medilabo.risklevel.service.impl;

import com.medilabo.backend.model.Patient;
import com.medilabo.risklevel.config.PatientServiceConfig;
import com.medilabo.risklevel.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private WebClient webclient;

    private PatientServiceConfig patientServiceConfig;

    @Autowired
    private PatientServiceImpl(PatientServiceConfig patientServiceConfig) {
        this.patientServiceConfig = patientServiceConfig;
    }


    @Override
    public Patient getPatient(String id) {
        return webclient.get()
                .uri(patientServiceConfig.getBaseUrl() + "/" + id)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }
}

package com.medilabo.risklevel.service.impl;

import com.medilabo.backend.model.Patient;
import com.medilabo.risklevel.config.PatientServiceConfig;
import com.medilabo.risklevel.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Patient service implementation that retrieves patient information using a WebClient.
 * Utilizes a PatientServiceConfig to determine the base URL for the requests.
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private WebClient webclient;

    private PatientServiceConfig patientServiceConfig;

    @Autowired
    private PatientServiceImpl(PatientServiceConfig patientServiceConfig) {
        this.patientServiceConfig = patientServiceConfig;
    }


    /**
     * Retrieves patient information by making a GET request using WebClient.
     * Constructs the request URL by appending the provided 'id' to the base URL fetched from PatientServiceConfig.
     * Converts the response body to a Mono of Patient class and blocks until the result is available.
     *
     * @param id The unique identifier of the patient to retrieve.
     * @return The patient information fetched from the specified URL.
     */
    @Override
    public Patient getPatient(String id) {
        return webclient.get()
                .uri(patientServiceConfig.getBaseUrl() + "/" + id)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }
}

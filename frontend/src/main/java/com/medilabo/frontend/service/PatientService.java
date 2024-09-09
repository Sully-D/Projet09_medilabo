package com.medilabo.frontend.service;

import com.medilabo.backend.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientService {

    @Value("${patient.service.base-url}")
    private String baseUrl; // Inject URL from application.yml

    @Autowired
    private WebClient webClient; // Utilisation de WebClient

    public List<Patient> getAllPatients() {
        return webClient.get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(Patient.class)
                .collectList()
                .block(); // Bloquer pour comportement synchrone
    }

    public Patient getPatientById(String id) {
        return webClient.get()
                .uri(baseUrl + "/" + id)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    public Patient createPatient(Patient patient) {
        return webClient.post()
                .uri(baseUrl)
                .bodyValue(patient)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    public Patient updatePatient(Patient patient) {
        return webClient.put()
                .uri(baseUrl + "/" + patient.getId())
                .bodyValue(patient)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    public void deletePatient(String id) {
        webClient.delete()
                .uri(baseUrl + "/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
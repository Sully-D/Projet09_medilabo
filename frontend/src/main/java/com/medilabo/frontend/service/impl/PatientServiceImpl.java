package com.medilabo.frontend.service.impl;

import com.medilabo.frontend.config.PatientConfig;
import com.medilabo.frontend.model.Patient;
import com.medilabo.frontend.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


/**
 * Service class for interacting with the patient service.
 * Provides methods for retrieving the list of all patients.
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private WebClient webClient;

    private PatientConfig patientConfig;

    @Autowired
    private HttpServletRequest request;  // Inject HttpServletRequest to access cookies

    @Autowired
    public PatientServiceImpl(PatientConfig patientConfig) {
        this.patientConfig = patientConfig;
    }

    /**
     * Retrieves a list of all patients by sending a GET request to the specified base URL.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Flux of Patient objects.
     * Blocks until the response is received and then collects the list of patients.
     *
     * @return List of all patients retrieved from the service
     */
    public List<Patient> getAllPatients() {
        return webClient.get()
                .uri(patientConfig.getBaseUrl())
                .retrieve()
                .bodyToFlux(Patient.class)
                .collectList()
                .block(); // Block for synchronous execution
    }

    /**
     * Retrieves a patient by their ID by sending a GET request to the specified base URL with the ID as a path variable.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of Patient objects.
     * Blocks until the response is received and then returns the patient.
     *
     * @param id the ID of the patient to retrieve
     * @return the patient retrieved from the service, or null if not found
     */
    public Patient getPatientById(String id) {
        return webClient.get()
                .uri(patientConfig.getBaseUrl() + "/" + id)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    /**
     * Creates a new patient by sending a POST request to the specified base URL with the provided patient as the request body.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of Patient objects.
     * Blocks until the response is received and then returns the created patient.
     *
     * @param patient the patient object to be created
     * @return the created patient retrieved from the service
     */
    public Patient createPatient(Patient patient) {
        return webClient.post()
                .uri(patientConfig.getBaseUrl())
                .bodyValue(patient)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    /**
     * Updates an existing patient by sending a PUT request to the specified base URL with the provided patient as the request body.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of Patient objects.
     * Blocks until the response is received and then returns the updated patient.
     *
     * @param patient the patient object to be updated
     * @return the updated patient retrieved from the service
     */
    public Patient updatePatient(Patient patient) {
        return webClient.put()
                .uri(patientConfig.getBaseUrl() + "/" + patient.getId())
                .bodyValue(patient)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    /**
     * Deletes a patient from the system by sending a DELETE request to the specified base URL with the ID of the patient to be deleted.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of ResponseEntity.
     * Blocks until the response is received and then returns void.
     *
     * @param id the ID of the patient to be deleted
     */
    public void deletePatient(String id) {
        webClient.delete()
                .uri(patientConfig.getBaseUrl() + "/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

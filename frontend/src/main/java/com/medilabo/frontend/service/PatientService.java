package com.medilabo.frontend.service;

import com.medilabo.frontend.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for interacting with the patient service.
 * Provides methods for retrieving the list of all patients.
 */
@Service
public interface PatientService {

    /**
     * Retrieves a list of all patients by sending a GET request to the specified base URL.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Flux of Patient objects.
     * Blocks until the response is received and then collects the list of patients.
     *
     * @return List of all patients retrieved from the service
     */
    public List<Patient> getAllPatients();

    /**
     * Retrieves a patient by their ID by sending a GET request to the specified base URL with the ID as a path variable.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of Patient objects.
     * Blocks until the response is received and then returns the patient.
     *
     * @param id the ID of the patient to retrieve
     * @return the patient retrieved from the service, or null if not found
     */
    public Patient getPatientById(String id);

    /**
     * Creates a new patient by sending a POST request to the specified base URL with the provided patient as the request body.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of Patient objects.
     * Blocks until the response is received and then returns the created patient.
     *
     * @param patient the patient object to be created
     * @return the created patient retrieved from the service
     */
    public Patient createPatient(Patient patient);

    /**
     * Updates an existing patient by sending a PUT request to the specified base URL with the provided patient as the request body.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of Patient objects.
     * Blocks until the response is received and then returns the updated patient.
     *
     * @param patient the patient object to be updated
     * @return the updated patient retrieved from the service
     */
    public Patient updatePatient(Patient patient);

    /**
     * Deletes a patient from the system by sending a DELETE request to the specified base URL with the ID of the patient to be deleted.
     * Uses WebClient to perform the request synchronously and retrieve the response as a Mono of ResponseEntity.
     * Blocks until the response is received and then returns void.
     *
     * @param id the ID of the patient to be deleted
     */
    public void deletePatient(String id);
}
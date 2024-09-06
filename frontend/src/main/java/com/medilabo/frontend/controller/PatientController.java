package com.medilabo.frontend.controller;

import com.medilabo.backend.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class PatientController {

    private static final String PATIENTS_API_PATH = "/api/patients";

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Value("${gateway.name:http://gateway}")
    private String gatewayUrl;

    private final RestTemplate restTemplate;

    public PatientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Retrieves a list of patients by sending a GET request to the specified gateway URL concatenated with the PATIENTS_API_PATH.
     * Logs the request and response details using the logger.
     * Handles different types of exceptions like HttpClientErrorException, ResourceAccessException, and generic exceptions, returning appropriate responses.
     * Returns a ResponseEntity containing the list of patients fetched from the response body.
     */
    @GetMapping("/patients")
    public ResponseEntity<Object> getPatients() {
        // Log the incoming request for patients data
        logger.info("Request received to fetch patients data.");
        logger.debug("Gateway URL: {}", gatewayUrl); // Debugging the gateway URL to verify it's correct

        try {
            // Prepare headers (if needed, you can add authentication tokens or other necessary headers)
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers); // Wrap the headers in an HttpEntity

            // Build the complete URL to send the request
            String url = gatewayUrl + PATIENTS_API_PATH;
            logger.info("Sending request to: {}", url);

            // Send the GET request and expect a list of patients in response
            ResponseEntity<List<Patient>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<Patient>>() {}
            );

            // Handle case where no patients are returned (204 No Content)
            List<Patient> patients = response.getBody();
            if (patients == null || patients.isEmpty()) {
                logger.warn("No patients data returned from the API.");
                return ResponseEntity.noContent().build(); // Return 204 if the list is empty
            }

            // Log the successful response and return the patients
            logger.info("Received response with patients data: {}", patients);
            return ResponseEntity.ok(patients);

        } catch (HttpClientErrorException e) {
            // Handle client-side errors (4xx HTTP errors)
            logger.error("Client error occurred: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("error", "Client error: " + e.getMessage()));
        } catch (ResourceAccessException e) {
            // Handle cases where the service is unavailable or there's a timeout
            logger.error("Service unavailable or timeout", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Collections.singletonMap("error", "Service unavailable or request timed out"));
        } catch (Exception e) {
            // Catch any other unexpected errors
            logger.error("Unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }
}
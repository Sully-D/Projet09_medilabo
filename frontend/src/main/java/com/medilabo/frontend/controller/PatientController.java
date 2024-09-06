package com.medilabo.frontend.controller;

import com.medilabo.backend.model.Patient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    /**
     * Creates a new patient by sending a POST request to the specified gateway URL concatenated with the PATIENTS_API_PATH.
     * Logs the request details and the response received using the logger.
     * Handles HttpClientErrorException by returning a response with the corresponding status code and error message.
     * Handles any other exceptions by returning an internal server error response with an error message.
     * Returns a ResponseEntity with the newly created patient in the response body if successful.
     */
    @PostMapping
    public ResponseEntity<Object> createPatient(@Valid @RequestBody Patient patient) {
        logger.info("Request received to create a new patient: {}", patient);

        try {
            // Prepare headers for the POST request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);

            // Build the full URL for the POST request
            String url = gatewayUrl + PATIENTS_API_PATH;
            logger.info("Sending POST request to: {}", url);

            // Send the request and get the response
            ResponseEntity<Patient> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST, entity, Patient.class
            );

            logger.info("Patient created successfully: {}", response.getBody());
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());

        } catch (HttpClientErrorException e) {
            // Log the specific HTTP error and return the appropriate response
            logger.error("Client error occurred while creating patient: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode())
                    .body(Collections.singletonMap("error", "Client error: " + e.getResponseBodyAsString()));
        } catch (Exception e) {
            // Handle unexpected exceptions and log them
            logger.error("Unexpected error occurred while creating patient", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }


    /**
     * Updates a patient with the provided ID by sending a PUT request to the gateway URL concatenated with the PATIENTS_API_PATH.
     * Sets the ID of the patient object to match the provided ID.
     * Handles HttpClientErrorException by returning a response with the corresponding status code and error message.
     * Handles any other exceptions by returning an internal server error response with an error message.
     * Returns a ResponseEntity with the updated patient in the response body if successful.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable String id, @Valid @RequestBody Patient patient) {
        logger.info("Request received to update patient with ID: {}", id);

        // Validate ID and patient data before proceeding
        if (id == null || id.isEmpty()) {
            logger.warn("Invalid patient ID provided: null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Patient ID cannot be null or empty"));
        }

        // Set the ID of the patient object to match the provided ID
        patient.setId(id);

        try {
            // Prepare headers for the PUT request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);

            // Build the full URL for the PUT request
            String url = gatewayUrl + PATIENTS_API_PATH + "/" + id;
            logger.info("Sending PUT request to: {}", url);

            // Send the request and get the response
            ResponseEntity<Patient> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT, entity, Patient.class
            );

            logger.info("Patient updated successfully: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            // Log the specific HTTP error and return the appropriate response
            logger.error("Client error occurred while updating patient: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode())
                    .body(Collections.singletonMap("error", "Client error: " + e.getResponseBodyAsString()));
        } catch (Exception e) {
            // Handle unexpected exceptions and log them
            logger.error("Unexpected error occurred while updating patient", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }


    /**
     * Deletes a patient by sending a DELETE request to the gateway URL concatenated with the PATIENTS_API_PATH and the patient ID.
     * Logs the request details and handles different exceptions like HttpClientErrorException and generic exceptions.
     * Returns a ResponseEntity with no content if the patient is deleted successfully.
     * If an error occurs, returns an appropriate response based on the type of exception encountered.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        logger.info("Request received to delete patient with ID: {}", id);

        // Validate the patient ID
        if (id == null || id.isEmpty()) {
            logger.warn("Invalid patient ID provided: null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            String url = gatewayUrl + PATIENTS_API_PATH + "/" + id;
            logger.info("Sending DELETE request to: {}", url);

            restTemplate.delete(url);

            logger.info("Patient with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();

        } catch (HttpClientErrorException e) {
            logger.error("Client error occurred while deleting patient: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting patient", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a patient by their first name and last name through a GET request to the specified gateway URL concatenated with the PATIENTS_API_PATH.
     * Logs the request details and handles exceptions like HttpClientErrorException and generic exceptions, providing appropriate responses.
     * Returns a ResponseEntity with the found patient in the response body if successful.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> getPatientByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Request received to search patient by name: {} {}", firstName, lastName);

        // Validate search parameters
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            logger.warn("Invalid search parameters: firstName or lastName is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "First name and last name must not be empty"));
        }

        try {
            // Encode the firstName and lastName to ensure the URL is valid
            String encodedFirstName = URLEncoder.encode(firstName, StandardCharsets.UTF_8);
            String encodedLastName = URLEncoder.encode(lastName, StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Construct the URL with encoded query parameters
            String url = gatewayUrl + PATIENTS_API_PATH + "/search?firstName=" + encodedFirstName + "&lastName=" + encodedLastName;
            logger.info("Sending GET request to: {}", url);

            // Send the GET request and capture the response
            ResponseEntity<Patient> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Patient.class
            );

            // Log the successful response
            logger.info("Patient found: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            // Log and handle HTTP client errors (4xx)
            logger.error("Client error occurred while searching for patient: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode())
                    .body(Collections.singletonMap("error", "Client error: " + e.getResponseBodyAsString()));
        } catch (ResourceAccessException e) {
            // Handle connectivity issues or service unavailability
            logger.error("Service unavailable or timeout while searching for patient", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Collections.singletonMap("error", "Service unavailable or request timed out"));
        } catch (Exception e) {
            // Log and handle any other unexpected exceptions
            logger.error("Unexpected error occurred while searching for patient", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }
}
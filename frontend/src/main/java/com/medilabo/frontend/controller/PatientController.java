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

    @Value("${gateway.name:https://gateway}")
    private String gatewayUrl;

    private final RestTemplate restTemplate;

    public PatientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients() {
        logger.info("Request received to fetch patients data.");
        logger.debug("Gateway URL: {}", gatewayUrl);

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            logger.info("Sending request to: {}{}", gatewayUrl, PATIENTS_API_PATH);
            ResponseEntity<List<Patient>> response = restTemplate.exchange(
                    gatewayUrl + PATIENTS_API_PATH,
                    HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<Patient>>() {}
            );

            logger.info("Received response with patients data: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Client error occurred: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (ResourceAccessException e) {
            logger.error("Service unavailable or timeout", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.emptyList());
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return ResponseEntity.internalServerError().body(Collections.emptyList());
        }
    }
}
package com.medilabo.frontend.controller;

import com.medilabo.backend.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class PatientController {

    // Logger SLF4J
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Value("${gateway.name:http://gateway}")
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
            // Créer les en-têtes HTTP et ajouter l'authentification Basic
            HttpHeaders headers = new HttpHeaders();

            // Créer l'entité HTTP en utilisant les en-têtes
            HttpEntity<String> entity = new HttpEntity<>(headers);

            logger.info("Sending request to: {}/api/patients", gatewayUrl);
            ResponseEntity<List<Patient>> response = restTemplate.exchange(
                    gatewayUrl + "/api/patients",
                    HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<Patient>>() {}
            );

            logger.info("Received response with patients data: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            logger.error("Error occurred while fetching patients data from the gateway", e);
            return ResponseEntity.internalServerError().body(Collections.emptyList());
        }
    }
}
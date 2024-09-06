package com.medilabo.frontend.service;

import com.medilabo.backend.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientService {

    private static final String BASE_URL = "http://localhost:8080/api/patients";

    @Autowired
    private RestTemplate restTemplate;

    public List<Patient> getAllPatients() {
        Patient[] patients = restTemplate.getForObject(BASE_URL, Patient[].class);
        return patients != null ? Arrays.asList(patients) : List.of();
    }

    public Patient getPatientById(String id) {
        return restTemplate.getForObject(BASE_URL + "/" + id, Patient.class);
    }

    public Patient createPatient(Patient patient) {
        return restTemplate.postForObject(BASE_URL, patient, Patient.class);
    }

    public Patient updatePatient(Patient patient) {
        restTemplate.put(BASE_URL + "/" + patient.getId(), patient);
        return patient;
    }

    public void deletePatient(String id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }
}
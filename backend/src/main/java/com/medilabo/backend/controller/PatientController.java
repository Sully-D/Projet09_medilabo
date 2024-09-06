package com.medilabo.backend.controller;

import com.medilabo.backend.model.Patient;
import com.medilabo.backend.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller handling CRUD operations for patients in the system.
 * Supports adding, updating, fetching all, searching by name, and deleting patients.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    /**
     * Adds a new patient to the system.
     *
     * @param patient The patient object to be added.
     * @return ResponseEntity with the added patient and HTTP status code.
     */
    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        logger.info("Received request to add a new patient: {}", patient);
        try {
            Patient savedPatient = patientService.add(patient);
            logger.info("Patient added successfully with ID: {}", savedPatient.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (RuntimeException e) {
            logger.error("Error occurred while adding patient: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Updates a patient in the system based on the provided ID.
     *
     * @param id The ID of the patient to be updated.
     * @param patient The updated patient object.
     * @return ResponseEntity with the updated patient and corresponding HTTP status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        logger.info("Received request to update patient with ID: {}", id);
        try {
            patient.setId(id);
            Patient updatedPatient = patientService.update(patient);
            if (updatedPatient == null) {
                logger.warn("Patient with ID: {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.info("Patient with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            logger.error("Error occurred while updating patient with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Retrieves all patients from the system.
     *
     * @return ResponseEntity with a list of all patients and corresponding HTTP status.
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        logger.info("Received request to fetch all patients");
        List<Patient> patients = patientService.getAll();
        logger.info("Fetched {} patients", patients.size());
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a patient by their first name and last name.
     *
     * @param firstName The first name of the patient to search for.
     * @param lastName The last name of the patient to search for.
     * @return ResponseEntity with the found patient and corresponding HTTP status, or a status of NOT_FOUND if the patient is not found.
     * In case of an error during the search, returns a BAD_REQUEST status.
     */
    @GetMapping("/search")
    public ResponseEntity<Patient> getPatientByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Received request to search patient by name: {} {}", firstName, lastName);
        try {
            Optional<Patient> patient = patientService.findOneByFirstNameAndLastName(firstName, lastName);
            if (patient.isPresent()) {
                logger.info("Patient found: {}", patient.get());
                return ResponseEntity.ok(patient.get());
            } else {
                logger.warn("Patient not found with name: {} {}", firstName, lastName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (RuntimeException e) {
            logger.error("Error occurred while searching for patient with name: {} {}", firstName, lastName, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Deletes a patient from the system based on the provided ID.
     *
     * @param id The ID of the patient to be deleted.
     * @return ResponseEntity with no content and HTTP status NO_CONTENT upon successful deletion.
     * If the patient is not found, returns a NOT_FOUND status.
     * In case of an error during deletion, returns a status of NOT_FOUND.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        logger.info("Received request to delete patient with ID: {}", id);
        try {
            patientService.delete(id);
            logger.info("Patient with ID: {} deleted successfully", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.error("Error occurred while deleting patient with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

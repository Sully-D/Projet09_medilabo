package com.medilabo.backend.controller;

import com.medilabo.backend.exceptions.EntityNotFoundException;
import com.medilabo.backend.model.Patient;
import com.medilabo.backend.service.PatientService;
import jakarta.validation.Valid;
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
     * @param patient The patient object to be added. It should contain valid data.
     * @return ResponseEntity with the added patient and HTTP status code.
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        logger.info("Received request to add a new patient: {}", patient);

        try {
            // Attempt to add the patient using the patient service
            Patient savedPatient = patientService.add(patient);
            logger.info("Patient added successfully with ID: {}", savedPatient.getId());

            // Return response with the created status and the saved patient
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);

        } catch (RuntimeException e) {
            // Log the error and return a bad request status with appropriate message
            logger.error("Error occurred while adding patient: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Consider returning a meaningful error message
        }
    }


    /**
     * Updates a patient in the system based on the provided ID.
     *
     * @param id The ID of the patient to be updated.
     * @param patient The updated patient object. The object should contain valid data.
     * @return ResponseEntity with the updated patient and corresponding HTTP status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @Valid @RequestBody Patient patient) {
        logger.info("Received request to update patient with ID: {}", id);

        try {
            // Set the patient ID before updating
            patient.setId(Long.valueOf(id));

            // Attempt to update the patient
            Patient updatedPatient = patientService.update(patient);
            if (updatedPatient == null) {
                // If the patient does not exist, return a 404 response
                logger.warn("Patient with ID: {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Consider returning a meaningful error message
            }

            // Log success and return updated patient
            logger.info("Patient with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedPatient);

        } catch (RuntimeException e) {
            // Log the error and return a bad request status
            logger.error("Error occurred while updating patient with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Consider returning a meaningful error message
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        logger.info("Received request to fetch patient with ID: {}", id);

        try {
            Optional<Patient> patient = patientService.getPatientById(id);
            if (patient.isPresent()) {
                return ResponseEntity.ok(patient.get());
            } else {
                logger.warn("Patient with ID: {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (RuntimeException e) {
            logger.error("Error occurred while fetching patient with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

        try {
            // Retrieve all patients
            List<Patient> patients = patientService.getAll();

            // Log the number of patients found
            if (patients.isEmpty()) {
                logger.warn("No patients found in the system");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(patients);  // 204 No Content if list is empty
            }

            logger.info("Fetched {} patients", patients.size());
            return ResponseEntity.ok(patients);

        } catch (RuntimeException e) {
            // Log the error and return a 500 status in case of a server-side error
            logger.error("Error occurred while fetching patients: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Retrieves a patient by their first name and last name.
     *
     * @param firstName The first name of the patient to search for. It should not be null or empty.
     * @param lastName The last name of the patient to search for. It should not be null or empty.
     * @return ResponseEntity with the found patient and corresponding HTTP status, or a status of NOT_FOUND if the patient is not found.
     * In case of an error during the search, returns a BAD_REQUEST status.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> getPatientByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        logger.info("Received request to search patient by name: {} {}", firstName, lastName);

        // Validate that the names are not null or empty
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            logger.warn("Invalid search parameters: firstName or lastName is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("First name and last name must not be empty");
        }

        try {
            // Attempt to find the patient by their first and last name
            Optional<Patient> patient = patientService.findOneByFirstNameAndLastName(firstName, lastName);

            // Check if a patient was found
            if (patient.isPresent()) {
                logger.info("Patient found: {}", patient.get());
                return ResponseEntity.ok(patient.get());
            } else {
                logger.warn("Patient not found with name: {} {}", firstName, lastName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Patient not found with name: " + firstName + " " + lastName);
            }

        } catch (RuntimeException e) {
            // Log the error and return a BAD_REQUEST status
            logger.error("Error occurred while searching for patient with name: {} {}", firstName, lastName, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error occurred while searching for the patient");
        }
    }


    /**
     * Deletes a patient from the system based on the provided ID.
     *
     * @param id The ID of the patient to be deleted. It should not be null or empty.
     * @return ResponseEntity with no content and HTTP status NO_CONTENT upon successful deletion.
     * If the patient is not found, returns a NOT_FOUND status.
     * In case of an error during deletion, returns a status of INTERNAL_SERVER_ERROR.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        logger.info("Received request to delete patient with ID: {}", id);

        // Validate the patient ID
        if (id == null || id.isEmpty()) {
            logger.warn("Invalid patient ID provided: null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            // Attempt to delete the patient
            patientService.delete(id);
            logger.info("Patient with ID: {} deleted successfully", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content on success

        } catch (EntityNotFoundException e) {
            // Log a specific error if the patient was not found
            logger.warn("Patient with ID: {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if patient doesn't exist

        } catch (RuntimeException e) {
            // Log and return a 500 status in case of any other server-side error
            logger.error("Error occurred while deleting patient with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error on other issues
        }
    }

}

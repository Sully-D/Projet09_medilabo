package com.medilabo.backend.service.impl;

import com.medilabo.backend.exceptions.DatabaseOperationException;
import com.medilabo.backend.exceptions.PatientAlreadyExistsException;
import com.medilabo.backend.exceptions.PatientNotFoundException;
import com.medilabo.backend.model.Patient;
import com.medilabo.backend.repository.PatientRepository;
import com.medilabo.backend.service.PatientService;
import com.medilabo.backend.util.PatientValidationService;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientValidationService patientValidationService;

    /**
     * Adds a new patient to the system.
     *
     * @param patient The patient object to be added.
     * @return The added patient if successful.
     * @throws PatientAlreadyExistsException if the patient already exists.
     * @throws ValidationException if validation fails.
     * @throws DatabaseOperationException if a database operation fails.
     */
    @Transactional
    @Override
    public Patient add(Patient patient) {
        logger.info("Attempting to add a new patient: {}", patient);

        try {
            // Validate the patient using a validation service
            patientValidationService.validatePatient(patient);

            // Check if the patient already exists
            Optional<Patient> isPatientExist = patientRepository.findByFirstNameAndLastNameAndDateOfBirth(
                    patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth()
            );

            if (isPatientExist.isPresent()) {
                // Log and throw a specific exception if the patient already exists
                logger.warn("Patient already exists with the provided details: {} {} {}",
                        patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());
                throw new PatientAlreadyExistsException("Patient already exists with these details.");
            }

            // Save the new patient to the repository
            Patient savedPatient = patientRepository.save(patient);
            logger.info("Patient added successfully with ID: {}", savedPatient.getId());
            return savedPatient;

        } catch (IllegalArgumentException e) {
            // Log and throw a specific validation exception
            logger.error("Validation failed while adding patient: {}", e.getMessage(), e);
            throw new ValidationException("Validation failed: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            // Log and throw a specific database operation exception
            logger.error("Database operation failed while adding patient: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Database operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing patient in the system.
     *
     * @param patient The patient object to be updated.
     * @return The updated patient if successful.
     * @throws PatientNotFoundException if the patient is not found.
     * @throws ValidationException if validation fails during the update process.
     * @throws DatabaseOperationException if there is an error during the database operation.
     */
    @Transactional
    @Override
    public Patient update(Patient patient) {
        logger.info("Attempting to update patient: {}", patient);

        try {
            // Validate the patient
            patientValidationService.validatePatient(patient);

            // Find the patient by ID (preferable to avoid potential issues with name duplicates)
            Optional<Patient> oldPatientEntry = patientRepository.findById(patient.getId());

            if (oldPatientEntry.isEmpty()) {
                // Log and throw a specific exception if the patient is not found
                logger.warn("Patient not found with ID: {}", patient.getId());
                throw new PatientNotFoundException("Patient with ID " + patient.getId() + " not found.");
            }

            // Update the patient data and save
            Patient updatedPatient = patientRepository.save(patient);
            logger.info("Patient updated successfully with ID: {}", updatedPatient.getId());
            return updatedPatient;

        } catch (IllegalArgumentException e) {
            // Log and throw a specific validation exception
            logger.error("Validation failed while updating patient: {}", e.getMessage(), e);
            throw new ValidationException("Validation failed: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            // Log and throw a specific database operation exception
            logger.error("Database operation failed while updating patient: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Database operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches all patients from the system.
     *
     * @return A list of all patients.
     * @throws DatabaseOperationException if there is an error during the database operation.
     */
    @Override
    public List<Patient> getAll() {
        logger.info("Fetching all patients");

        try {
            List<Patient> patients = patientRepository.findAll();

            if (patients.isEmpty()) {
                logger.warn("No patients found in the system.");
            } else {
                logger.info("Fetched {} patients", patients.size());
            }

            return patients;

        } catch (DataAccessException e) {
            // Log the error and throw a specific database operation exception
            logger.error("Error occurred while fetching all patients: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Database operation failed while fetching all patients: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for a patient by first name and last name.
     *
     * @param firstName The first name of the patient to search for.
     * @param lastName The last name of the patient to search for.
     * @return An Optional containing the patient if found, otherwise empty.
     * @throws ValidationException if validation fails during the search process.
     */
    @Override
    public Optional<Patient> findOneByFirstNameAndLastName(String firstName, String lastName) {
        logger.info("Searching for patient by first name: {} and last name: {}", firstName, lastName);

        try {
            // Validate the first and last name
            patientValidationService.isNameEmpty(firstName, lastName);

            // Search for the patient in the repository
            Optional<Patient> patient = patientRepository.findByFirstNameAndLastName(firstName, lastName).stream().findFirst();

            // Log whether the patient was found
            if (patient.isPresent()) {
                logger.info("Patient found: {}", patient.get());
            } else {
                logger.warn("No patient found with the name: {} {}", firstName, lastName);
            }

            return patient;

        } catch (IllegalArgumentException e) {
            // Log and throw a specific validation exception
            logger.error("Validation failed while searching for patient: {}", e.getMessage(), e);
            throw new ValidationException("Validation failed: " + e.getMessage(), e);

        } catch (DataAccessException e) {
            // Log and throw a specific database operation exception if any DB issue occurs
            logger.error("Database operation failed while searching for patient: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Database operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a patient from the system based on the provided ID.
     *
     * @param id The ID of the patient to be deleted.
     * @throws PatientNotFoundException if the patient with the provided ID is not found.
     * @throws DatabaseOperationException if a database operation fails during the deletion process.
     */
    @Override
    public void delete(String id) {
        logger.info("Attempting to delete patient with ID: {}", id);

        // Validate the ID
        if (id == null || id.isEmpty()) {
            logger.warn("Invalid patient ID provided: null or empty");
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }

        try {
            // Check if the patient exists before attempting to delete
            if (!patientRepository.existsById(id)) {
                logger.warn("Patient with ID: {} not found", id);
                throw new PatientNotFoundException("Patient with ID " + id + " not found.");
            }

            // Delete the patient by ID
            patientRepository.deleteById(id);
            logger.info("Patient with ID: {} deleted successfully", id);

        } catch (DataAccessException e) {
            // Log and throw a specific database operation exception
            logger.error("Database operation failed while deleting patient with ID: {}", id, e);
            throw new DatabaseOperationException("Database operation failed: " + e.getMessage(), e);
        }
    }

}

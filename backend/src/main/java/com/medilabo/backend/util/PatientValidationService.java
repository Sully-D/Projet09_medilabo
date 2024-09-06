package com.medilabo.backend.util;

import com.medilabo.backend.exceptions.ValidationException;
import com.medilabo.backend.model.Patient;
import org.springframework.stereotype.Service;


/**
 * Validates a patient object to ensure essential fields are not null or empty.
 * Throws a ValidationException with specific messages if validation fails for:
 * - Patient object being null
 * - Patient's first name being null or empty
 * - Patient's last name being null or empty
 * - Patient's date of birth being null
 *
 * Checks if the first name or last name is null or empty.
 * Throws an IllegalArgumentException if either first name or last name is null or empty.
 */
@Service
public class PatientValidationService {

    /**
     * Validates a patient object to ensure essential fields are not null or empty.
     * Throws a ValidationException with specific messages if validation fails for:
     * - Patient object being null
     * - Patient's first name being null or empty
     * - Patient's last name being null or empty
     * - Patient's date of birth being null
     */
    public void validatePatient(Patient patient) {
        if (patient == null) {
            throw new ValidationException("Patient object cannot be null");
        }

        // Validate first name
        if (patient.getFirstName() == null || patient.getFirstName().isEmpty()) {
            throw new ValidationException("Patient's first name cannot be null or empty");
        }

        // Validate last name
        if (patient.getLastName() == null || patient.getLastName().isEmpty()) {
            throw new ValidationException("Patient's last name cannot be null or empty");
        }

        // Validate date of birth
        if (patient.getDateOfBirth() == null) {
            throw new ValidationException("Patient's date of birth cannot be null");
        }
    }

    /**
     * Checks if the first name or last name is null or empty.
     * Throws an IllegalArgumentException if either first name or last name is null or empty.
     */
    public void isNameEmpty (String firstName, String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("FirstName and LastName can't empty");
        }
    }
}
package com.medilabo.backend.exceptions;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException(String message) {
        super(message);
    }

    public PatientAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
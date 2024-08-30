package com.medilabo.backend.repository;


import com.medilabo.backend.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Patient> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, String dateOfBirth);
}
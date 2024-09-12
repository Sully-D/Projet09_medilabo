package com.medilabo.backend.repository;


import com.medilabo.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Patient> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, String dateOfBirth);

    Optional<Patient> getPatientById(Long id);
}
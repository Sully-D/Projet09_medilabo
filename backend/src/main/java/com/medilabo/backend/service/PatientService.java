package com.medilabo.backend.service;


import com.medilabo.backend.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PatientService {

    Patient add (Patient patient);

    Patient update (Patient patient);

    List<Patient> getAll ();

    Optional<Patient> findOneByFirstNameAndLastName(String firstName, String lastName);

    void delete (String id);

    Optional<Patient> getPatientById(String id);
}
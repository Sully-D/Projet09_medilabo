package com.medilabo.risklevel.service;

import com.medilabo.backend.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface PatientService {

    List<Patient> getPatient(String id);
}

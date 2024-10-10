package com.medilabo.risklevel.service;


import com.medilabo.risklevel.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface PatientService {

    Patient getPatient(String id);
}

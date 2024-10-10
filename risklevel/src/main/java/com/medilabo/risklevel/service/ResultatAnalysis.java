package com.medilabo.risklevel.service;


import com.medilabo.risklevel.model.Patient;
import org.springframework.stereotype.Service;


@Service
public interface ResultatAnalysis {

    String levelOfRisk(int nbSymptoms, Patient patient);
}

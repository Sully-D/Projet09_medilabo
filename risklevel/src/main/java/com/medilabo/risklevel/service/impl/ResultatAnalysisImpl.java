package com.medilabo.risklevel.service.impl;

import com.medilabo.backend.model.Patient;
import com.medilabo.risklevel.service.ResultatAnalysis;
import com.medilabo.risklevel.util.AgeCalculator;
import org.springframework.stereotype.Service;


@Service
public class ResultatAnalysisImpl implements ResultatAnalysis {

    @Override
    public String levelOfRisk(int nbSymptoms, Patient patient) {

        String earlyOnset = "EarlyOnset";
        String inDanger = "InDanger";
        String borderline = "Borderline";

        int agePatient = AgeCalculator.calculateAge(patient.getDateOfBirth());

        // Early onset
        if (patient.getGender().toUpperCase() == "M" && agePatient < 30) {
            if (nbSymptoms >= 5) {
                return earlyOnset;
            }
        } else if (patient.getGender().toUpperCase() == "F" && agePatient < 30) {
            if (nbSymptoms >= 7) {
                return earlyOnset;
            }
        }
        if (agePatient > 30) {
            if (nbSymptoms >= 8) {
                return earlyOnset;
            }
        }

        // In danger
        if (patient.getGender().toUpperCase() == "M" && agePatient < 30) {
            if (nbSymptoms == 3) {
                return inDanger;
            }
        } else if (patient.getGender().toUpperCase() == "F" && agePatient < 30) {
            if (nbSymptoms == 4) {
                return inDanger;
            }
        }
        if (agePatient > 30) {
            if (nbSymptoms >= 6 && nbSymptoms <= 7) {
                return inDanger;
            }
        }

        // Borderline
        if (agePatient > 30) {
            if (nbSymptoms >= 2 && nbSymptoms <= 5) {
                return borderline;
            }
        }

        return "None";
    }
}

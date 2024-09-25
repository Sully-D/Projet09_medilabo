package com.medilabo.risklevel.service.impl;

import com.medilabo.backend.model.Patient;
import com.medilabo.risklevel.service.ResultatAnalysis;
import com.medilabo.risklevel.util.AgeCalculator;
import org.springframework.stereotype.Service;


@Service
public class ResultatAnalysisImpl implements ResultatAnalysis {

    @Override
    public String levelOfRisk(int nbSymptoms, Patient patient) {

        // Constantes level of risk
        final String EARLY_ONSET = "EarlyOnset";
        final String IN_DANGER = "InDanger";
        final String BORDERLINE = "Borderline";
        final String NONE = "None";

        // Constantes for gender
        final String MALE = "M";
        final String FEMALE = "F";

        int agePatient = AgeCalculator.calculateAge(patient.getDateOfBirth());
        String gender = patient.getGender();

        // Early onset conditions
        if (agePatient < 30) {
            if (MALE.equalsIgnoreCase(gender) && nbSymptoms >= 5) {
                return EARLY_ONSET;
            }
            if (FEMALE.equalsIgnoreCase(gender) && nbSymptoms >= 7) {
                return EARLY_ONSET;
            }
        } else if (agePatient >= 30 && nbSymptoms >= 8) {
            return EARLY_ONSET;
        }

        // In danger conditions
        if (agePatient < 30) {
            if (MALE.equalsIgnoreCase(gender) && nbSymptoms == 3) {
                return IN_DANGER;
            }
            if (FEMALE.equalsIgnoreCase(gender) && nbSymptoms == 4) {
                return IN_DANGER;
            }
        } else if (agePatient >= 30 && nbSymptoms >= 6 && nbSymptoms <= 7) {
            return IN_DANGER;
        }

        // Borderline condition
        if (agePatient >= 30 && nbSymptoms >= 2 && nbSymptoms <= 5) {
            return BORDERLINE;
        }

        // None Risk
        return NONE;
    }

}

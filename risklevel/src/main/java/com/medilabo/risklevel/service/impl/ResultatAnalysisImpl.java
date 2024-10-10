package com.medilabo.risklevel.service.impl;

import com.medilabo.risklevel.model.Patient;
import com.medilabo.risklevel.service.ResultatAnalysis;
import com.medilabo.risklevel.util.AgeCalculator;
import org.springframework.stereotype.Service;


/**
 * Service implementation for analyzing the level of risk based on the number of symptoms and patient information.
 * Implements the ResultatAnalysis interface.
 * Utilizes an enum RiskLevel for categorizing risk levels and provides methods to check for different risk levels.
 */
@Service
public class ResultatAnalysisImpl implements ResultatAnalysis {

    // Enum for risk level
    private enum RiskLevel {
        EARLY_ONSET, IN_DANGER, BORDERLINE, NONE;

        // Get formatted name
        public String getFormattedName() {
            String[] words = this.name().toLowerCase().split("_");
            StringBuilder formattedName = new StringBuilder();
            for (String word : words) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            }
            return formattedName.toString();
        }
    }


    // Gender constants
    private static final String MALE = "M";
    private static final String FEMALE = "F";


    /**
     * Calculates the level of risk based on the number of symptoms and patient information.
     * Determines the risk level by checking if the patient is in the early onset, in danger, borderline, or no risk category.
     *
     * @param nbSymptoms The number of symptoms the patient is experiencing.
     * @param patient The patient object containing information like age and gender.
     * @return A string representing the calculated risk level (EARLY_ONSET, IN_DANGER, BORDERLINE, NONE).
     */
    @Override
    public String levelOfRisk(int nbSymptoms, Patient patient) {
        int age = AgeCalculator.calculateAge(patient.getDateOfBirth());
        String gender = patient.getGender();

        if (isEarlyOnset(age, nbSymptoms, gender)) {
            return RiskLevel.EARLY_ONSET.getFormattedName();
        }

        if (isInDanger(age, nbSymptoms, gender)) {
            return RiskLevel.IN_DANGER.getFormattedName();
        }

        if (isBorderline(age, nbSymptoms)) {
            return RiskLevel.BORDERLINE.getFormattedName();
        }

        return RiskLevel.NONE.getFormattedName();
    }

    /**
     * Checks if a patient has early onset based on age, number of symptoms, and gender.
     *
     * @param age the age of the patient
     * @param nbSymptoms the number of symptoms the patient has
     * @param gender the gender of the patient
     * @return true if the patient has early onset, false otherwise
     */
    private boolean isEarlyOnset(int age, int nbSymptoms, String gender) {
        if (age < 30) {
            return (MALE.equalsIgnoreCase(gender) && nbSymptoms >= 5) ||
                    (FEMALE.equalsIgnoreCase(gender) && nbSymptoms >= 7);
        } else {
            return nbSymptoms >= 8;
        }
    }

    /**
     * Checks if a patient is in danger based on age, number of symptoms, and gender.
     *
     * @param age the age of the patient
     * @param nbSymptoms the number of symptoms the patient is experiencing
     * @param gender the gender of the patient
     * @return true if the patient is in danger, false otherwise
     */
    private boolean isInDanger(int age, int nbSymptoms, String gender) {
        if (age < 30) {
            return (MALE.equalsIgnoreCase(gender) && nbSymptoms == 3) ||
                    (FEMALE.equalsIgnoreCase(gender) && nbSymptoms == 4);
        } else {
            return nbSymptoms >= 6 && nbSymptoms <= 7;
        }
    }

    /**
     * Checks if a patient's risk level is borderline based on age and number of symptoms.
     *
     * @param age the age of the patient
     * @param nbSymptoms the number of symptoms the patient is experiencing
     * @return true if the risk level is borderline, false otherwise
     */
    private boolean isBorderline(int age, int nbSymptoms) {
        return age >= 30 && nbSymptoms >= 2 && nbSymptoms <= 5;
    }
}

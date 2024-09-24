package com.medilabo.risklevel.util;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;


public class AgeCalculator {

    public static int calculateAge(String birthDateString) {

        // Convertir la chaîne de caractères en LocalDate
        LocalDate birthDate = LocalDate.parse(birthDateString);

        // Obtenir la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Calculer l'âge
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}

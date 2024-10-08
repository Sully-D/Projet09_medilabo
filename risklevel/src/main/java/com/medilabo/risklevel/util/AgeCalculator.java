package com.medilabo.risklevel.util;

import java.time.LocalDate;
import java.time.Period;

/**
 * Calculates the age based on the given birthDateString string.
 * @return the age calculated from the birthDateString to the current date
 */
public class AgeCalculator {

    public static int calculateAge(String birthDateString) {

        LocalDate birthDate = LocalDate.parse(birthDateString);

        LocalDate currentDate = LocalDate.now();

        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}

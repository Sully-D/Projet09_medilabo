package com.medilabo.risklevel.util;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;


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

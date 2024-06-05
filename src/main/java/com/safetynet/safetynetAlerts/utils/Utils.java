package com.safetynet.safetynetAlerts.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
public class Utils {
    public static int convertToAge(String birthdateString) throws Exception {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthdate = LocalDate.parse(birthdateString, formatter);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthdate, currentDate).getYears();
        } catch (Exception e) {
            throw new Exception("Invalid date format ! Valid : 'MM/dd/yyyy");
        }
    }
}

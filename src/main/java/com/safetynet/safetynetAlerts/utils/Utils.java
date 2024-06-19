package com.safetynet.safetynetAlerts.utils;

import com.safetynet.safetynetAlerts.controllers.PersonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Utility class providing helper methods for common operations.
 * <p>
 * This class contains static methods for performing various utility operations, such as converting a
 * birthdate string to age.
 * </p>
 */
@Service
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * Converts a birthdate string to age.
     *
     * @param birthdateString The birthdate string to convert (in 'MM/dd/yyyy' format).
     * @return The age calculated from the birthdate.
     * @throws Exception If the birthdate string is invalid or in an incorrect format.
     */
    public static int convertToAge(String birthdateString) throws Exception {
        logger.info("Entered convertToAge method");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthdate = LocalDate.parse(birthdateString, formatter);
            LocalDate currentDate = LocalDate.now();
            logger.info("Birthday String properly converted to age");
            return Period.between(birthdate, currentDate).getYears();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new Exception("Invalid date format. Should be of pattern : MM/dd/yyyy");
        }
    }
}

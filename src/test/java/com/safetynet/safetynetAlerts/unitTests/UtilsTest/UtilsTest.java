package com.safetynet.safetynetAlerts.unitTests.UtilsTest;

import com.safetynet.safetynetAlerts.utils.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsTest {

    @Test
    void testConvertToAgeSuccess() throws Exception {
        String birthdateString = "01/31/2010";
        int age = Utils.convertToAge(birthdateString);
        assertEquals(age, 14);
    }

    @Test
    void testConvertToAgeWrongFormat() {
        String birthdateString = "31/01/2010";
        assertThrows(Exception.class, () -> Utils.convertToAge(birthdateString));
    }
}

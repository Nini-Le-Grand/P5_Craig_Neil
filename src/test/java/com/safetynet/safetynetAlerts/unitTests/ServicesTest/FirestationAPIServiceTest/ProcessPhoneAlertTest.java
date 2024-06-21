package com.safetynet.safetynetAlerts.unitTests.ServicesTest.FirestationAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.FirestationAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessPhoneAlertTest {
    @Mock
    private PersonDAO personDAO;

    @Mock
    private FirestationDAO firestationDAO;

    @InjectMocks
    private FirestationAPIService firestationAPIService;

    @InjectMocks
    private FirestationData firestationData;

    @InjectMocks
    private PersonData personData;

    @BeforeEach
    void before() {
        firestationAPIService.setFirestationDAO(firestationDAO);
        firestationAPIService.setPersonDAO(personDAO);
    }

    @Test
    void testProcessPhoneAlertSuccess() throws Exception {
        List<Firestation> firestations = firestationData.getListOfFirestation();
        List<Person> persons = personData.getListOfPersons();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(persons);

        List<String> phones = firestationAPIService.processPhoneAlert(anyString());
        assertEquals(phones.size(), 2);
        assertEquals(phones.get(1), "1111111111");
    }

    @Test
    void testProcessPhoneAlertWrongStation() {
        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processPhoneAlert(anyString()));
        assertEquals(exception.getMessage(), "Cannot find Firestation");
    }

    @Test
    void testProcessPhoneAlertWrongAddresses() {
        List<Firestation> firestations = firestationData.getListOfFirestation();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processPhoneAlert(anyString()));
        assertEquals(exception.getMessage(), "Cannot find Person");
    }
}

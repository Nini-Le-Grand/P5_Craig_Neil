package com.safetynet.safetynetAlerts.unitTests.ServicesTest.FirestationAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.FirestationDTO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.FirestationAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessFirestationTest {
    @Mock
    private PersonDAO personDAO;

    @Mock
    private FirestationDAO firestationDAO;

    @Mock
    private MedicalRecordDAO medicalRecordDAO;

    @InjectMocks
    private FirestationAPIService firestationAPIService;

    @InjectMocks
    private PersonData personData;

    @InjectMocks
    private FirestationData firestationData;

    @InjectMocks
    private MedicalRecordData medicalRecordData;

    @BeforeEach
    void before() {
        firestationAPIService.setFirestationDAO(firestationDAO);
        firestationAPIService.setPersonDAO(personDAO);
        firestationAPIService.setMedicalRecordDAO(medicalRecordDAO);
    }

    @Test
    void testProcessFirestationSuccess() throws Exception {
        List<Firestation> firestations = firestationData.getListOfFirestation();
        List<Person> persons = personData.getListOfPersons();
        List<MedicalRecord> medicalRecords = medicalRecordData.getListOfMedicalRecord();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.of(medicalRecords.getFirst()))
                .thenReturn(Optional.of(medicalRecords.getLast()));

        FirestationDTO returnDTO = firestationAPIService.processFirestation("1");
        assertEquals(returnDTO.getChildren(), 1);
        assertEquals(returnDTO.getAdults(), 1);
        assertEquals(returnDTO.getPersons().size(), 2);
    }

    @Test
    void testProcessFirestationWrongStation() {
        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFirestation("1"));
        assertEquals(exception.getMessage(), "Cannot find firestation");
    }

    @Test
    void testProcessFirestationEmptyAddress() {
        List<Firestation> firestations = firestationData.getListOfFirestation();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFirestation("1"));
        assertEquals(exception.getMessage(), "Cannot find person");
    }

    @Test
    void testProcessFirestationEmptyMedicalRecord() {
        List<Firestation> firestations = firestationData.getListOfFirestation();
        List<Person> persons = personData.getListOfPersons();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFirestation("1"));
        assertEquals(exception.getMessage(), "Cannot find medical record");
    }

}

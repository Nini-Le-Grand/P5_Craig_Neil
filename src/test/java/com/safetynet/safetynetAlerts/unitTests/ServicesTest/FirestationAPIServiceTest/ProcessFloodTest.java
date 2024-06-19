package com.safetynet.safetynetAlerts.unitTests.ServicesTest.FirestationAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.FloodDTO;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessFloodTest {
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
    void testProcessFloodSuccess() throws Exception {
        List<Firestation> firestations = firestationData.getListOfFirestation();
        List<Person> persons = personData.getListOfPersons();
        List<MedicalRecord> medicalRecords = medicalRecordData.getListOfMedicalRecord();

        String[] stations = {"1"};

        when(firestationDAO.collectFirestation("1"))
                .thenReturn(firestations);
        when(personDAO.collectOnAddress(anyString()))
                .thenReturn(List.of(persons.getFirst()))
                .thenReturn(List.of(persons.getLast()));
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.of(medicalRecords.getFirst()))
                .thenReturn(Optional.of(medicalRecords.getLast()));

        List<FloodDTO> returnDTO = firestationAPIService.processFlood(stations);
        assertEquals(returnDTO.size(), 1);
        assertEquals(returnDTO.getFirst().getStation(), "1");
        assertEquals(returnDTO.getFirst().getAddresses().size(), 2);
        assertEquals(returnDTO.getFirst().getAddresses().getFirst().getPersons().getFirst().getFirstName(), "Neil");
        assertEquals(returnDTO.getFirst().getAddresses().getLast().getPersons().getFirst().getFirstName(), "Laura");

    }

    @Test
    void testProcessFloodNoFirestation() {
        String[] stations = {"1"};

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFlood(stations));
        assertEquals(exception.getMessage(), "Cannot find firestation");
    }

    @Test
    void testProcessFloodNoPerson() {
        String[] stations = {"1"};

        List<Firestation> firestations = firestationData.getListOfFirestation();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddress(anyString()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFlood(stations));
        assertEquals(exception.getMessage(), "Cannot find person");
    }

    @Test
    void testProcessFloodNoMedicalRecord() {
        String[] stations = {"1"};

        List<Firestation> firestations = firestationData.getListOfFirestation();
        List<Person> persons = personData.getListOfPersons();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);
        when(personDAO.collectOnAddress(anyString()))
                .thenReturn(List.of(persons.getFirst()));
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFlood(stations));
        assertEquals(exception.getMessage(), "Cannot find medical record");
    }
}

package com.safetynet.safetynetAlerts.unitTests.ServicesTest.FirestationAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.APIDTOs.FireDTO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.APIServices.FirestationAPIService;
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
public class ProcessFireTest {
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
    void testProcessFireSuccess() throws Exception {
        Firestation firestation = firestationData.getFireStation();
        List<Person> persons = personData.getListOfPersons();
        List<MedicalRecord> medicalRecords = medicalRecordData.getListOfMedicalRecord();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.of(firestation));
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.of(medicalRecords.getFirst()))
                .thenReturn(Optional.of(medicalRecords.getLast()));

        FireDTO returnDTO = firestationAPIService.processFire(anyString());
        assertEquals(returnDTO.getStation(), "5");
        assertEquals(returnDTO.getPersons().size(), 2);
        assertEquals(returnDTO.getPersons().getFirst().getFirstName(), "Neil");
        assertEquals((returnDTO.getPersons().getFirst().getAge()), 32);
        assertEquals(returnDTO.getPersons().getLast().getFirstName(), "Laura");
        assertEquals(returnDTO.getPersons().getLast().getAge(), 14);
    }

    @Test
    void testProcessFireNoFirestation() {
        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFire(anyString()));
        assertEquals(exception.getMessage(), "Cannot find firestation");
    }

    @Test
    void testProcessFireNoPerson() {
        Firestation firestation = firestationData.getFireStation();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.of(firestation));
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFire(anyString()));
        assertEquals(exception.getMessage(), "Cannot find person");
    }

    @Test
    void testProcessFireNoMedicalRecord() {
        Firestation firestation = firestationData.getFireStation();
        List<Person> persons = personData.getListOfPersons();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.of(firestation));
        when(personDAO.collectOnAddresses(anyList()))
                .thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> firestationAPIService.processFire(anyString()));
        assertEquals(exception.getMessage(), "Cannot find medical record");
    }
}

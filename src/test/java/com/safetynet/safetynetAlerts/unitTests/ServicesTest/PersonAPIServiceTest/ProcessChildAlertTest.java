package com.safetynet.safetynetAlerts.unitTests.ServicesTest.PersonAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.ChildAlertDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.PersonAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessChildAlertTest {
    @Mock
    private PersonDAO personDAO;

    @Mock
    private MedicalRecordDAO medicalRecordDAO;

    @InjectMocks
    private PersonAPIService personAPIService;

    @InjectMocks
    private PersonData personData;

    @InjectMocks
    private MedicalRecordData medicalRecordData;

    @BeforeEach
    void before() {
        personAPIService.setPersonDAO(personDAO);
        personAPIService.setMedicalRecordDAO(medicalRecordDAO);
    }

    @Test
    void processChildAlertSuccessWithChildren() throws Exception {
        List<Person> persons = personData.getListOfPersons();
        List<MedicalRecord> medicalRecords = medicalRecordData.getListOfMedicalRecord();

        when(personDAO.collectOnAddresses(anyList())).thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString())).thenReturn(Optional.of(medicalRecords.getFirst())).thenReturn(Optional.of(medicalRecords.getLast()));

        ChildAlertDTO returnDTO = personAPIService.processChildAlert(anyString());
        assertEquals(returnDTO.getChildren().size(), 1);
        assertEquals(returnDTO.getAdults().size(), 1);
        assertEquals(returnDTO.getChildren().getFirst().getAge(), 14);
        assertEquals(returnDTO.getAdults().getFirst().getFirstName(), "Neil");
    }

    @Test
    void processChildAlertSuccessWithoutChildren() throws Exception {
        List<Person> persons = personData.getListOfPersons();
        List<MedicalRecord> medicalRecords = medicalRecordData.getListOfMedicalRecord();
        medicalRecords.get(1).setBirthdate("01/01/2000");

        when(personDAO.collectOnAddresses(anyList())).thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString())).thenReturn(Optional.of(medicalRecords.getFirst())).thenReturn(Optional.of(medicalRecords.getLast()));

        ChildAlertDTO returnDTO = personAPIService.processChildAlert(anyString());
        assertNull(returnDTO);
    }

    @Test
    void testProcessChildAlertNoPerson() {
        when(personDAO.collectOnAddresses(anyList())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> personAPIService.processChildAlert(anyString()));
        assertEquals(exception.getMessage(), "Cannot find Person");
    }

    @Test
    void testProcessChildAlertNoMedicalRecord() {
        List<Person> persons = personData.getListOfPersons();

        when(personDAO.collectOnAddresses(anyList())).thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> personAPIService.processChildAlert(anyString()));
        assertEquals(exception.getMessage(), "Cannot find MedicalRecord");
    }
}

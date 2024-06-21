package com.safetynet.safetynetAlerts.unitTests.ServicesTest.PersonAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.PersonInfoDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessPersonInfoTest {
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
    void testProcessPersonInfoSuccess() throws Exception {
        List<Person> persons = List.of(personData.getPerson());
        List<MedicalRecord> medicalRecords = List.of(medicalRecordData.getMedicalRecord());

        when(personDAO.collectPerson(anyString(), anyString())).thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString())).thenReturn(Optional.of(medicalRecords.getFirst()));

        List<PersonInfoDTO> returnDTO = personAPIService.processPersonInfo(persons.getFirst().getFirstName(),
                persons.getFirst().getLastName());
        assertEquals(returnDTO.size(), 1);
        assertEquals(returnDTO.getFirst().getFirstName(), "Neil");
        assertEquals(returnDTO.getFirst().getAge(), 32);
    }

    @Test
    void testProcessPersonInfoSuccessWithSiblings() throws Exception {
        List<Person> persons = List.of(personData.getPerson(), personData.getPerson());
        List<MedicalRecord> medicalRecords = List.of(medicalRecordData.getMedicalRecord(),
                medicalRecordData.getMedicalRecord());
        medicalRecords.getLast().setBirthdate("01/01/2010");

        when(personDAO.collectPerson(anyString(), anyString())).thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString())).thenReturn(Optional.of(medicalRecords.getFirst())).thenReturn(Optional.of(medicalRecords.getLast()));

        List<PersonInfoDTO> returnDTO = personAPIService.processPersonInfo("Neil", "Craig");
        assertEquals(returnDTO.size(), 2);
        assertEquals(returnDTO.getFirst().getFirstName(), "Neil");
        assertEquals(returnDTO.getFirst().getAge(), 32);
        assertEquals(returnDTO.getLast().getFirstName(), "Neil");
        assertEquals(returnDTO.getLast().getAge(), 14);
    }

    @Test
    void testProcessPersonInfoNoPerson() {
        when(personDAO.collectPerson(anyString(), anyString())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> personAPIService.processPersonInfo(anyString(),
                anyString()));
        assertEquals(exception.getMessage(), "Cannot find Person");
    }

    @Test
    void testProcessPersonInfoNoMedicalRecord() {
        List<Person> persons = List.of(personData.getPerson());

        when(personDAO.collectPerson(anyString(), anyString())).thenReturn(persons);
        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> personAPIService.processPersonInfo(anyString(),
                anyString()));
        assertEquals(exception.getMessage(), "Cannot find MedicalRecord");
    }
}

package com.safetynet.safetynetAlerts.unitTests.ServicesTest;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.*;
import com.safetynet.safetynetAlerts.services.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MedicalRecordServiceTest {
    @Mock
    private MedicalRecordDAO medicalRecordDAO;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordData medicalRecordData;

    @InjectMocks
    private PersonData personData;

    @BeforeEach
    void before() {
        medicalRecordService.setMedicalRecordDAO(medicalRecordDAO);
    }

    @Test
    void testSaveMedicalRecordSuccess() throws Exception {
        MedicalRecord medicalRecord = medicalRecordData.getMedicalRecord();

        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.empty());

        medicalRecordService.saveMedicalRecord(medicalRecord);
        verify(medicalRecordDAO, times(1)).addMedicalRecord(medicalRecord);
    }

    @Test
    void testSaveMedicalRecordFailure() {
        MedicalRecord medicalRecord = medicalRecordData.getMedicalRecord();

        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.of(medicalRecord));

        Exception exception = assertThrows(Exception.class,
                () -> medicalRecordService.saveMedicalRecord(medicalRecord));
        assertEquals(exception.getMessage(), "MedicalRecord already saved");
    }

    @Test
    void testEditMedicalRecordSuccess() throws Exception {
        MedicalRecord medicalRecord = medicalRecordData.getMedicalRecord();
        MedicalRecordUpdateDTO medicalRecordUpdateDTO = medicalRecordData.getMedicalRecordUpdateDTO();

        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.of(medicalRecord));

        medicalRecordService.editMedicalRecord(medicalRecordUpdateDTO);
        verify(medicalRecordDAO, times(1)).setMedicalRecords(medicalRecordUpdateDTO, medicalRecord);
    }

    @Test
    void testEditMedicalRecordFailure() {
        MedicalRecordUpdateDTO medicalRecordUpdateDTO = medicalRecordData.getMedicalRecordUpdateDTO();

        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class,
                () -> medicalRecordService.editMedicalRecord(medicalRecordUpdateDTO));
        assertEquals(exception.getMessage(), "Cannot find MedicalRecord to edit");
    }

    @Test
    void testDeleteMedicalRecordSuccess() throws Exception {
        MedicalRecord medicalRecord = medicalRecordData.getMedicalRecord();
        PersonIdDTO personIdDTO = personData.getPersonIdDTO();

        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.of(medicalRecord));

        medicalRecordService.deleteMedicalRecord(personIdDTO);
        verify(medicalRecordDAO, times(1)).removeMedicalRecord(medicalRecord);
    }

    @Test
    void testDeleteMedicalRecordFailure() {
        PersonIdDTO personIdDTO = personData.getPersonIdDTO();

        when(medicalRecordDAO.findMedicalRecord(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class,
                () -> medicalRecordService.deleteMedicalRecord(personIdDTO));
        assertEquals(exception.getMessage(), "Cannot find MedicalRecord to delete");
    }
}

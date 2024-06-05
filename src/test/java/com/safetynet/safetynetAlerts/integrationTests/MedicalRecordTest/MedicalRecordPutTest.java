package com.safetynet.safetynetAlerts.integrationTests.MedicalRecordTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.DAO.JSONDataDAO;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordPutTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataDAO jsonDataDAO;

    @InjectMocks
    private MedicalRecordData medicalRecordData;

    private MedicalRecordUpdateDTO medicalRecordUpdateDTO;

    @BeforeEach
    void setUp() throws Exception {
        jsonDataDAO.loadDataFromFile();

        MedicalRecord medicalRecord = medicalRecordData.getMedicalRecord();
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)));

        medicalRecordUpdateDTO = medicalRecordData.getMedicalRecordUpdateDTO();
    }

    @Test
    void testHandlePutMedicalRecord_Success() throws Exception {
        List<String> medications = List.of("Doliprane: 500mg", "Advil: 200mg");
        medicalRecordUpdateDTO.setMedications(medications);
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical record successfully edited"));
    }

    @Test
    void testHandlePutMedicalRecordWithEmptyList_Success() throws Exception {
        medicalRecordUpdateDTO.setMedications(List.of());
        medicalRecordUpdateDTO.setAllergies(List.of());
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical record successfully edited"));
    }

    @Test
    void testHandlePutMedicalRecordNoChanges_Success() throws Exception {
        medicalRecordUpdateDTO.setBirthdate("");
        medicalRecordUpdateDTO.setMedications(null);
        medicalRecordUpdateDTO.setAllergies(null);
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical record successfully edited"));
    }

    @Test
    void testHandlePutMedicalRecordWithoutChangingMeds_Success() throws Exception {
        medicalRecordUpdateDTO.setMedications(null);
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical record successfully edited"));
    }

    @Test
    void testHandlePutPerson_BadRequest_BlankField() throws Exception {
        medicalRecordUpdateDTO.setFirstName("");
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePutMedicalRecord.medicalRecordDTO.firstName:" +
                        " must not be blank"));

        medicalRecordUpdateDTO.setFirstName("Neil");
        medicalRecordUpdateDTO.setLastName("");
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePutMedicalRecord.medicalRecordDTO.lastName: " +
                        "must not be blank"));
    }

    @Test
    void testHandlePutPerson_NotFoundException() throws Exception {
        medicalRecordUpdateDTO.setFirstName("unknonwn");
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find medical record to edit"));
    }
}

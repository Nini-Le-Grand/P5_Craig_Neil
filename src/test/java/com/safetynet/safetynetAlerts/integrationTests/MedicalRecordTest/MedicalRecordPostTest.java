package com.safetynet.safetynetAlerts.integrationTests.MedicalRecordTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.DataSet;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordPostTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSet dataSet;

    @InjectMocks
    private MedicalRecordData medicalRecordData;

    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() throws IOException {
        dataSet.loadDataFromFile();

        medicalRecord = medicalRecordData.getMedicalRecord();
    }

    @Test
    void testHandlePostMedicalRecord_Success() throws Exception {
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Medical record successfully saved"));
    }

    @Test
    void testHandlePostMedicalRecord_BadRequest_BlankField() throws Exception {
        medicalRecord.setFirstName(null);
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostMedicalRecord.medicalRecord.firstName: " +
                        "must not be blank"));

        medicalRecord.setFirstName("");
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostMedicalRecord.medicalRecord.firstName: " +
                        "must not be blank"));

        medicalRecord.setFirstName(" ");
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostMedicalRecord.medicalRecord.firstName: " +
                        "must not be blank"));

        medicalRecord.setFirstName("Neil");
        medicalRecord.setLastName(null);
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostMedicalRecord.medicalRecord.lastName: " +
                        "must not be blank"));

        medicalRecord.setFirstName("Neil");
        medicalRecord.setLastName("Craig");
        medicalRecord.setBirthdate(null);
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostMedicalRecord.medicalRecord.birthdate: " +
                        "must not be blank"));
    }

    @Test
    void testHandlePostMedicalRecord_BadRequest_NullField() throws Exception {
        medicalRecord.setMedications(null);
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostMedicalRecord.medicalRecord.medications:" +
                        " must not be null"));
    }

    @Test
    void testHandlePostPerson_Conflict_Exception() throws Exception {
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("MedicalRecord already saved"));

    }
}

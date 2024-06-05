package com.safetynet.safetynetAlerts.integrationTests.MedicalRecordTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.DAO.JSONDataDAO;
import com.safetynet.safetynetAlerts.UtilsData.MedicalRecordData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordDeleteTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataDAO jsonDataDAO;

    @InjectMocks
    private MedicalRecordData medicalRecordData;

    @InjectMocks
    private PersonData personData;

    private PersonIdDTO personIdDTO;

    @BeforeEach
    void setUp() throws Exception {
        jsonDataDAO.loadDataFromFile();

        MedicalRecord medicalRecord = medicalRecordData.getMedicalRecord();
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)));

        personIdDTO = personData.getPersonIdDTO();
    }

    @Test
    void testHandleDeleteMedicalRecord_Success() throws Exception {
        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personIdDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical record successfully deleted"));
    }

    @Test
    void testHandleDeleteMedicalRecord_BadRequest_BlankField() throws Exception {
        personIdDTO.setFirstName(null);
        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personIdDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handleDeleteMedicalRecord.idPersonDTO.firstName: " +
                        "must not be blank"));

        personIdDTO.setFirstName("");
        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personIdDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handleDeleteMedicalRecord.idPersonDTO.firstName: " +
                        "must not be blank"));

        personIdDTO.setFirstName(" ");
        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personIdDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handleDeleteMedicalRecord.idPersonDTO.firstName: " +
                        "must not be blank"));

        personIdDTO.setFirstName("Neil");
        personIdDTO.setLastName(null);
        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personIdDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handleDeleteMedicalRecord.idPersonDTO.lastName: " +
                        "must not be blank"));
    }

    @Test
    void testHandleDeleteMedicalRecord_NotFoundException() throws Exception {
        personIdDTO.setFirstName("unknown");
        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personIdDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find medical record to delete"));

    }
}

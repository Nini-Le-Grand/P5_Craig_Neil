package com.safetynet.safetynetAlerts.integrationTests.FirestationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.DataSet;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.models.Firestation;
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
public class FirestationDeleteTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSet dataSet;

    @InjectMocks
    private FirestationData firestationData;

    @BeforeEach
    void setUp() throws Exception {
        dataSet.loadDataFromFile();

        Firestation firestation = firestationData.getFireStation();
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestation)));
    }

    @Test
    void testHandleDeleteFirestationByAddress_Success() throws Exception {
        String address = "10 Downing Street";
        mockMvc.perform(delete("/firestation/{addressOrStation}", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully deleted"));
    }

    @Test
    void testHandleDeleteFirestationByStation_Success() throws Exception {
        String station = "1";
        mockMvc.perform(delete("/firestation/{addressOrStation}", station)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully deleted"));
    }


    @Test
    void testHandleDeleteFirestation_BadRequest_BlankField() throws Exception {
        String pathVariable = " ";
        mockMvc.perform(delete("/firestation/{addressOrStation}", pathVariable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handleDeleteFirestation.addressOrStation: must not" +
                        " be blank"));
    }

    @Test
    void testHandleDeleteFirestationByAddress_NotFoundException() throws Exception {
        String pathVariable = "0";
        mockMvc.perform(delete("/firestation/{addressOrStation}", pathVariable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find station to delete"));
    }

    @Test
    void testHandleDeleteFirestationByStation_NotFoundException() throws Exception {
        String pathVariable = "Unknown";
        mockMvc.perform(delete("/firestation/{addressOrStation}", pathVariable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find address to delete"));
    }
}

package com.safetynet.safetynetAlerts.integrationTests.FirestationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.JSONDataLoader;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationPutTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataLoader jsonDataLoader;

    @InjectMocks
    private FirestationData firestationData;

    private Firestation firestation;

    @BeforeEach
    void setUp() throws Exception {
        jsonDataLoader.loadDataFromFile();

        firestation = firestationData.getFireStation();
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestation)));
    }

    @Test
    void testHandlePutFirestation_Success() throws Exception {
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully edited"));
    }

    @Test
    void testHandlePutFirestation_BadRequest_BlankField() throws Exception {
        firestation.setStation(null);
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePutFirestation.firestation.station: must not" +
                        " be blank"));

        firestation.setStation("2");
        firestation.setAddress(null);
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePutFirestation.firestation.address: must not" +
                        " be blank"));
    }

    @Test
    void testHandlePutFirestation_NotFoundException() throws Exception {
        firestation.setAddress("unknonwn");
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find firestation to edit"));
    }
}

package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.DataSet;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationGetTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSet dataSet;

    @InjectMocks
    private PersonData personData;

    @InjectMocks
    private FirestationData firestationData;

    private Person person;

    private Firestation firestation;

    @BeforeEach
    void setup() throws Exception {
        dataSet.loadDataFromFile();
        person = personData.getPerson();
        firestation = firestationData.getFireStation();
    }

    @Test
    void testHandleGetFirestation_Success() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.persons[0].firstName").value("Peter"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Duncan"))
                .andExpect(jsonPath("$.persons[0].address").value("644 Gershwin Cir"))
                .andExpect(jsonPath("$.persons[0].city").value("Culver"))
                .andExpect(jsonPath("$.persons[0].zip").value("97451"))
                .andExpect(jsonPath("$.persons[0].phone").value("841-874-6512"))
                .andExpect(jsonPath("$.adults").value(5))
                .andExpect(jsonPath("$.children").value(1));
    }

    @Test
    void testHandleGetFirestation_FailureFirestationNotFound() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "unknown")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find Firestation"));
    }

    @Test
    void testHandleGetFirestation_FailurePersonNotFound() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully saved"));

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find Person"));
    }

    @Test
    void testHandleGetFirestation_FailureMedicalRecordNotFound() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully saved"));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person successfully saved"));

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find MedicalRecord"));
    }

    @Test
    void testHandleGetFirestation_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetFirestation.stationNumber: must not be blank"));
    }
}

package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.DAO.JSONDataDAO;
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
    private JSONDataDAO jsonDataDAO;

    @InjectMocks
    private PersonData personData;

    @InjectMocks
    private FirestationData firestationData;

    private Person person;

    private Firestation firestation;

    @BeforeEach
    void setup() throws Exception {
        jsonDataDAO.loadDataFromFile();

        person = personData.getPerson();
        firestation = firestationData.getFireStation();
    }

    @Test
    void testFirestationGet_Success() throws Exception {
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
    void testFireGet_FailureFirestationNotFound() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "unknown")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find firestation"));
    }

    @Test
    void testFireGet_FailurePersonNotFound() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully saved"));

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find person"));
    }

    @Test
    void testFireGet_FailureMedicalRecordNotFound() throws Exception {
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
                .andExpect(content().string("Cannot find medical record"));
    }

    @Test
    void testFireGet_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("getFirestation.station: must not be blank"));
    }
}
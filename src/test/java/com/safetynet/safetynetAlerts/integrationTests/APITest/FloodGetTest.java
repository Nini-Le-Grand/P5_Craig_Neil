package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.JSONDataLoader;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FloodGetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataLoader jsonDataLoader;

    @InjectMocks
    private PersonData personData;

    @InjectMocks
    private FirestationData firestationData;

    private Person person;

    private Firestation firestation;

    @BeforeEach
    void setup() throws Exception {
        jsonDataLoader.loadDataFromFile();
        person = personData.getPerson();
        firestation = firestationData.getFireStation();
    }

    @Test
    void testHandleGetFlood_SuccessOneParam() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[0].addresses").isArray())
                .andExpect(jsonPath("$[0].addresses", hasSize(3)))
                .andExpect(jsonPath("$[0].addresses[0].address").value("644 Gershwin Cir"))
                .andExpect(jsonPath("$[0].addresses[0].persons").isArray())
                .andExpect(jsonPath("$[0].addresses[0].persons", hasSize(1)))
                .andExpect(jsonPath("$[0].addresses[0].persons[0].firstName").value("Peter"))
                .andExpect(jsonPath("$[0].addresses[0].persons[0].lastName").value("Duncan"))
                .andExpect(jsonPath("$[0].addresses[0].persons[0].phone").value("841-874-6512"))
                .andExpect(jsonPath("$[0].addresses[0].persons[0].age").value(23))
                .andExpect(jsonPath("$[0].addresses[0].persons[0].medications").isArray())
                .andExpect(jsonPath("$[0].addresses[0].persons[0].medications").isEmpty())
                .andExpect(jsonPath("$[0].addresses[0].persons[0].allergies").isArray())
                .andExpect(jsonPath("$[0].addresses[0].persons[0].allergies[0]").value("shellfish"));
    }

    @Test
    void testHandleGetFlood_SuccessMultipleParam() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1")
                        .param("stations", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[1].station").value("2"));
    }

    @Test
    void testHandleGetFlood_FailureFirestationNotFound() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "unknown")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find firestation"));
    }

    @Test
    void testHandleGetFlood_FailurePersonNotFound() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully saved"));

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find person"));
    }

    @Test
    void testHandleGetFlood_FailureMedicalRecordNotFound() throws Exception {
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

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find medical record"));
    }

    @Test
    void testHandleGetFlood_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetFlood.stations: must not be empty"));
    }
}

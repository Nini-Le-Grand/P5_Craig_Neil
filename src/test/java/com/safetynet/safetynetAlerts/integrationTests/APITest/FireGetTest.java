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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FireGetTest {

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
    void testHandleGetFire_Success() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "834 Binoc Ave")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.persons[0].firstName").value("Tessa"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Carman"))
                .andExpect(jsonPath("$.persons[0].phone").value("841-874-6512"))
                .andExpect(jsonPath("$.persons[0].age").value(12))
                .andExpect(jsonPath("$.persons[0].medications").isArray())
                .andExpect(jsonPath("$.persons[0].medications").isEmpty())
                .andExpect(jsonPath("$.persons[0].allergies").isArray())
                .andExpect(jsonPath("$.persons[0].allergies").isEmpty())
                .andExpect(jsonPath("$.station").value("3"));
    }

    @Test
    void testHandleGetFire_FailureFirestationNotFound() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "Unknown address")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find firestation"));
    }

    @Test
    void testHandleGetFire_FailurePersonNotFound() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully saved"));


        mockMvc.perform(get("/fire")
                        .param("address", "10 Downing Street")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find person"));
    }

    @Test
    void testHandleGetFire_FailureMedicalRecordNotFound() throws Exception {
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


        mockMvc.perform(get("/fire")
                        .param("address", "10 Downing Street")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find medical record"));
    }

    @Test
    void testHandleGetFire_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetFire.address: must not be blank"));
    }
}

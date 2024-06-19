package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.JSONDataLoader;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
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
public class ChildAlertGetTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataLoader jsonDataLoader;

    @InjectMocks
    private PersonData personData;

    private Person person;

    @BeforeEach
    void setup() throws Exception {
        jsonDataLoader.loadDataFromFile();
        person = personData.getPerson();
    }

    @Test
    void testHandleGetChildAlert_SuccessWithChildren() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "947 E. Rose Dr")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children[0].firstName").value("Kendrik"))
                .andExpect(jsonPath("$.children[0].lastName").value("Stelzer"))
                .andExpect(jsonPath("$.children[0].age").value(10))
                .andExpect(jsonPath("$.adults").isArray())
                .andExpect(jsonPath("$.adults[0].firstName").value("Brian"))
                .andExpect(jsonPath("$.adults[0].lastName").value("Stelzer"))
                .andExpect(jsonPath("$.adults[1].firstName").value("Shawna"))
                .andExpect(jsonPath("$.adults[1].lastName").value("Stelzer"));
    }

    @Test
    void testHandleGetChildAlert_SuccessWithNoChildren() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "908 73rd St")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testHandleGetChildAlert_FailurePersonNotFound() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "unknown address")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find person"));
    }

    @Test
    void testHandleGetChildAlert_FailureMedicalRecordNotFound() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person successfully saved"));

        mockMvc.perform(get("/childAlert")
                        .param("address", "10 Downing Street")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find medical record"));
    }

    @Test
    void testHandleGetChildAlert_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetChildAlert.address: must not be blank"));
    }
}

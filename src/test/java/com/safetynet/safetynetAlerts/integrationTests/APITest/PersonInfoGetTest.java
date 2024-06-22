package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.DataSet;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonInfoGetTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSet dataSet;

    @InjectMocks
    private PersonData personData;

    private Person person;

    @BeforeEach
    void setup() throws Exception {
        dataSet.loadDataFromFile();
        person = personData.getPerson();
    }

    @Test
    void testHandleGetPersonInfo_Success() throws Exception {
        mockMvc.perform(get("/personInfo")
                        .param("firstName", "John")
                        .param("lastName", "Boyd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[0].city").value("Culverr"))
                .andExpect(jsonPath("$[0].zip").value("97451"))
                .andExpect(jsonPath("$[0].email").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[0].age").value(40))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$[0].medications[1]").value("hydrapermazol:100mg"))
                .andExpect(jsonPath("$[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$[0].allergies[0]").value("nillacilan"));
    }

    @Test
    void testHandleGetPersonInfo_FailurePersonNotFound() throws Exception {
        mockMvc.perform(get("/personInfo")
                        .param("firstName", "Neil")
                        .param("lastName", "Craig")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find Person"));

        mockMvc.perform(get("/personInfo")
                        .param("firstName", "John")
                        .param("lastName", "Unknown")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find Person"));
    }

    @Test
    void testHandleGetPersonInfo_FailureMedicalRecordNotFound() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person successfully saved"));

        mockMvc.perform(get("/personInfo")
                        .param("firstName", "Neil")
                        .param("lastName", "Craig")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find MedicalRecord"));
    }

    @Test
    void testHandleGetPersonInfo_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/personInfo")
                        .param("firstName", "")
                        .param("lastName", "Craig")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetPersonInfo.firstName: must not be blank"));

        mockMvc.perform(get("/personInfo")
                        .param("firstName", "Neil")
                        .param("lastName", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetPersonInfo.lastName: must not be blank"));
    }
}

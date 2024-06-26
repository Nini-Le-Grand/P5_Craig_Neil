package com.safetynet.safetynetAlerts.integrationTests.PersonTest;

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

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class PersonPostTest {
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
    void setUp() throws IOException {
        dataSet.loadDataFromFile();

        person = personData.getPerson();
    }

    @Test
    void testHandlePostPerson_Success() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person successfully saved"));
    }

    @Test
    void testHandlePostPerson_BadRequest_BlankField() throws Exception {
        person.setFirstName(null);
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostPerson.person.firstName: must not be " +
                        "blank"));

        person.setFirstName("");
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostPerson.person.firstName: must not be " +
                        "blank"));

        person.setFirstName(" ");
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostPerson.person.firstName: must not be " +
                        "blank"));

        person.setFirstName("Neil");
        person.setLastName(null);
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostPerson.person.lastName: must not be " +
                        "blank"));

        person.setFirstName("Neil");
        person.setLastName("Craig");
        person.setPhone(null);
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handlePostPerson.person.phone: must not be blank"));
    }

    @Test
    void testHandlePostPerson_Conflict_Exception() throws Exception {
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
               .andExpect(MockMvcResultMatchers.status().isConflict())
               .andExpect(MockMvcResultMatchers.content().string("Person already saved"));

    }
}

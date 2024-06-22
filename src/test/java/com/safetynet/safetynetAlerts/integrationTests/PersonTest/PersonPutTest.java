package com.safetynet.safetynetAlerts.integrationTests.PersonTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.DataSet;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
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
class PersonPutTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSet dataSet;

    @InjectMocks
    private PersonData personData;

    private PersonUpdateDTO personUpdateDTO;

    @BeforeEach
    void setUp() throws Exception {
        dataSet.loadDataFromFile();

        personUpdateDTO = personData.getPersonUpdateDTO();

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personUpdateDTO)));
    }

    @Test
    void testHandlePutPerson_Success() throws Exception {
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Person successfully edited"));
    }

    @Test
    void testHandlePutPerson_SuccessNoChanges() throws Exception {
        personUpdateDTO.setAddress("");
        personUpdateDTO.setCity("");
        personUpdateDTO.setZip("");
        personUpdateDTO.setPhone("");
        personUpdateDTO.setEmail("");
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Person successfully edited"));
    }

    @Test
    void testHandlePutPerson_BadRequest_BlankField() throws Exception {
        personUpdateDTO.setFirstName(null);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string("handlePutPerson.personDTO.firstName: must not be blank"));

        personUpdateDTO.setFirstName("Neil");
        personUpdateDTO.setLastName(null);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string("handlePutPerson.personDTO.lastName: must not be blank"));
    }

    @Test
    void testHandlePutPerson_BadRequest_NullField() throws Exception {
        personUpdateDTO.setPhone(null);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string("handlePutPerson.personDTO.phone: must not be null"));

    }

    @Test
    void testHandlePutPerson_NotFoundException() throws Exception {
        personUpdateDTO.setFirstName("unknonwn");
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Cannot find Person to edit"));
    }
}

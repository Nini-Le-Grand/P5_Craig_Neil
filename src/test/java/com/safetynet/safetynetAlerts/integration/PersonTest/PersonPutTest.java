package com.safetynet.safetynetAlerts.integration.PersonTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.DAO.JSONDataDAO;
import com.safetynet.safetynetAlerts.models.JSONData;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private PersonUpdateDTO personUpdateDTO;

    @BeforeEach
    void setUp() throws Exception {
        personUpdateDTO = new PersonUpdateDTO();
        personUpdateDTO.setFirstName("Neil");
        personUpdateDTO.setLastName("Craig");
        personUpdateDTO.setAddress("13 rue des dames");
        personUpdateDTO.setCity("Paris");
        personUpdateDTO.setZip("75017");
        personUpdateDTO.setPhone("0102030405");
        personUpdateDTO.setEmail("neil@craig.com");

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
                .andExpect(MockMvcResultMatchers.content().string("Person successfully updated"));
    }

    @Test
    void testHandlePutPerson_BadRequest_BlankField() throws Exception {
        personUpdateDTO.setFirstName(null);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("firstName must not be blank"));

        personUpdateDTO.setFirstName("Neil");
        personUpdateDTO.setLastName(null);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("lastName must not be blank"));
    }

    @Test
    void testHandlePostPerson_BadRequest_NullField() throws Exception {
        personUpdateDTO.setPhone(null);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("phone must not be null"));

    }

    @Test
    void testHandlePostPerson_NotFound() throws Exception {
        personUpdateDTO.setFirstName("unknonwn");
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find person to edit"));
    }
}

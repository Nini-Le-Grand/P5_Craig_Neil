package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.JSONDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommunityEmailGetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataLoader jsonDataLoader;

    @BeforeEach
    void setup() throws Exception {
        jsonDataLoader.loadDataFromFile();
    }

    @Test
    void testHandleGetCommunityEmails_Success() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*]").isNotEmpty())
                .andExpect(jsonPath("$[*]", everyItem(matchesPattern("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))));
    }

    @Test
    void testHandleGetCommunityEmails_FailurePersonNotFound() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Unknown city")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Cannot find person"));
    }

    @Test
    void testHandleGetCommunityEmails_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("handleGetCommunityEmails.city: must not be blank"));
    }
}

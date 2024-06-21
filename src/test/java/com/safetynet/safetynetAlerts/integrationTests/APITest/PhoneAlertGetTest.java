package com.safetynet.safetynetAlerts.integrationTests.APITest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.data.JSONDataLoader;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.models.Firestation;
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
public class PhoneAlertGetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONDataLoader jsonDataLoader;

    @InjectMocks
    private FirestationData firestationData;

    private Firestation firestation;

    @BeforeEach
    void setup() throws Exception {
        jsonDataLoader.loadDataFromFile();

        firestation = firestationData.getFireStation();
    }

    @Test
    void testHandleGetPhoneAlert_Success() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpectAll(jsonPath("$[0]").isString());
    }

    @Test
    void testHandleGetPhoneAlert_FailureFirestationNotFound() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find Firestation"));
    }

    @Test
    void testHandleGetPhoneAlert_FailurePersonNotFound() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Firestation successfully saved"));

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot find Person"));
    }

    @Test
    void testHandleGetPhoneAlert_FailureEmptyParam() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("handleGetPhoneAlert.firestation: must not be blank"));
    }
}

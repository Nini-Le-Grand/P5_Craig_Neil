package com.safetynet.safetynetAlerts.unitTests.ServicesTest.PersonAPIServiceTest;

import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.PersonAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessCommunityEmailTest {
    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private PersonAPIService personAPIService;

    @InjectMocks
    private PersonData personData;

    @BeforeEach
    void before() {
        personAPIService.setPersonDAO(personDAO);
    }

    @Test
    void testProcessCommunityEmailSuccess() throws Exception {
        List<Person> persons = personData.getListOfPersons();

        when(personDAO.collectOnCity(anyString())).thenReturn(persons);

        List<String> returnDTO = personAPIService.processCommunityEmail(anyString());
        assertEquals(returnDTO.size(), 2);
        assertEquals(returnDTO.getFirst(), "neil@craig.com");
        assertEquals(returnDTO.getLast(), "laura@delvine.com");
    }

    @Test
    void testProcessCommunityEmailNoPerson() {
        when(personDAO.collectOnCity(anyString())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> personAPIService.processCommunityEmail(anyString()));
        assertEquals(exception.getMessage(), "Cannot find person");
    }
}

package com.safetynet.safetynetAlerts.unitTests.ServicesTest;

import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.UtilsData.PersonData;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import com.safetynet.safetynetAlerts.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonServiceTest {
    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private PersonService personService;

    @InjectMocks
    private PersonData personData;

    @BeforeEach
    void before() {
        personService.setPersonDAO(personDAO);
    }

    @Test
    void testSavePersonSuccess() throws Exception {
        Person person = personData.getPerson();

        when(personDAO.findPerson(anyString(), anyString()))
                .thenReturn(Optional.empty());

        personService.savePerson(person);
        verify(personDAO, times(1)).addPerson(person);
    }

    @Test
    void testSavePersonFailure() {
        Person person = personData.getPerson();

        when(personDAO.findPerson(anyString(), anyString()))
                .thenReturn(Optional.of(person));

        Exception exeption = assertThrows(Exception.class, () -> personService.savePerson(person));
        assertEquals(exeption.getMessage(), "Person already saved");
    }

    @Test
    void testEditPersonSuccess() throws Exception {
        Person person = personData.getPerson();
        PersonUpdateDTO personUpdateDTO = personData.getPersonUpdateDTO();

        when(personDAO.findPerson(anyString(), anyString()))
                .thenReturn(Optional.of(person));

        personService.editPerson(personUpdateDTO);
        verify(personDAO, times(1)).setPerson(personUpdateDTO, person);
    }

    @Test
    void testEditPersonFailure() {
        PersonUpdateDTO personUpdateDTO = personData.getPersonUpdateDTO();

        when(personDAO.findPerson(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> personService.editPerson(personUpdateDTO));
        assertEquals(exception.getMessage(), "Cannot find Person to edit");
    }

    @Test
    void testDeletePersonSuccess() throws Exception {
        Person person = personData.getPerson();
        PersonIdDTO personId = personData.getPersonIdDTO();

        when(personDAO.findPerson(anyString(), anyString()))
                .thenReturn(Optional.of(person));

        personService.deletePerson(personId);
        verify(personDAO, times(1)).removePerson(person);
    }

    @Test
    void testDeletePersonFailure() {
        PersonIdDTO personId = personData.getPersonIdDTO();

        when(personDAO.findPerson(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> personService.deletePerson(personId));
        assertEquals(exception.getMessage(), "Cannot find Person to delete");
    }
}

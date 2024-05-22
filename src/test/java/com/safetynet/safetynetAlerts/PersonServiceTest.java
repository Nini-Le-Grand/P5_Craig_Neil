package com.safetynet.safetynetAlerts;

import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.exceptions.ConflictException;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonServiceTest {
    @Mock
    private PersonDAO personDAO;

    @Autowired
    private PersonService personService;

    @BeforeEach
    void before() {
        personService.setPersonDAO(personDAO);
    }

     @Test
    void testSavePerson() throws ConflictException {
        when(personDAO.findPerson(anyString(), anyString())).thenReturn(Optional.empty());

        Person person = new Person();
        person.setFirstName("Neil");
        person.setLastName("Craig");
        person.setAddress("13 rue des dames");
        person.setCity("Paris");
        person.setZip("75017");
        person.setPhone("0102030405");
        person.setEmail("neil@craig.com");

        personService.savePerson(person);
        verify(personDAO, times(1)).addPerson(eq(person));
    }
}

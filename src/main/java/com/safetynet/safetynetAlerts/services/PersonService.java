package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import com.safetynet.safetynetAlerts.models.Person;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class PersonService {

    @Autowired
    private PersonDAO personDAO;

    public void savePerson(Person person) throws Exception {
        if (personDAO.findPerson(person.getFirstName(), person.getLastName()).isEmpty()) {
            personDAO.addPerson(person);
        } else {
            throw new Exception("Person already saved");
        }
    }

    public void editPerson(PersonUpdateDTO person) throws Exception {
        Optional<Person> optionalPerson = personDAO.findPerson(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            personDAO.setPerson(person, optionalPerson.get());
        } else {
            throw new Exception("Cannot find person to edit");
        }
    }

    public void deletePerson(PersonIdDTO person) throws Exception {
        Optional<Person> optionalPerson = personDAO.findPerson(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            personDAO.removePerson(optionalPerson.get());
        } else {
            throw new Exception("Cannot find person to delete");
        }
    }
}
package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import com.safetynet.safetynetAlerts.exceptions.ConflictException;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.Person;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Service
public class PersonService {

    @Autowired
    private PersonDAO personDAO;

    public void savePerson(Person person) throws ConflictException {
        if (personDAO.findPerson(person.getFirstName(), person.getLastName()).isEmpty()) {
            personDAO.addPerson(person);
        } else {
            throw new ConflictException("Person already saved in db");
        }
    }

    public void editPerson(PersonUpdateDTO person) throws NotFoundException {
        Optional<Person> optionalPerson = personDAO.findPerson(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            personDAO.setPerson(person, optionalPerson.get());
        } else {
            throw new NotFoundException("Cannot find person to edit");
        }
    }

    public void deletePerson(PersonIdDTO person) throws NotFoundException {
        Optional<Person> optionalPerson = personDAO.findPerson(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            personDAO.removePerson(optionalPerson.orElse(null));
        } else {
            throw new NotFoundException("Cannot find person to delete");
        }
    }
}
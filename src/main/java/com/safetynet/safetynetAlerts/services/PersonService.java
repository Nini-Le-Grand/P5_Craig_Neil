package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.controllers.PersonController;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import com.safetynet.safetynetAlerts.models.Person;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling operations related to {@link Person} entity.
 */
@Setter
@Service
public class PersonService {

    @Autowired
    private PersonDAO personDAO;

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    /**
     * Saves a new {@link Person} entity to the data set.
     *
     * @param person the Person entity to save
     * @throws Exception if the person already exists in the data set
     */
    public void savePerson(Person person) throws Exception {
        logger.info("Checking if Person exists in data set");
        if (personDAO.findPerson(person.getFirstName(), person.getLastName()).isEmpty()) {
            personDAO.addPerson(person);
        } else {
            logger.error("Person already exists in data set");
            throw new Exception("Person already saved");
        }
    }

    /**
     * Edits an existing {@link Person} entity in the data set.
     *
     * @param person the PersonUpdateDTO containing updated information
     * @throws Exception if the person does not exist in the data set
     */
    public void editPerson(PersonUpdateDTO person) throws Exception {
        logger.info("Checking if Person exists in data set");
        Optional<Person> optionalPerson = personDAO.findPerson(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            personDAO.setPerson(person, optionalPerson.get());
        } else {
            logger.error("Cannot find Person in data set");
            throw new Exception("Cannot find Person to edit");
        }
    }

    /**
     * Deletes an existing {@link Person} entity from the data set.
     *
     * @param person the PersonIdDTO containing the ID information of the person to delete
     * @throws Exception if the person does not exist in the data set
     */
    public void deletePerson(PersonIdDTO person) throws Exception {
        logger.info("Checking if Person exists in data set");
        Optional<Person> optionalPerson = personDAO.findPerson(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            personDAO.removePerson(optionalPerson.get());
        } else {
            logger.error("Cannot find Person in data set");
            throw new Exception("Cannot find Person to delete");
        }
    }
}
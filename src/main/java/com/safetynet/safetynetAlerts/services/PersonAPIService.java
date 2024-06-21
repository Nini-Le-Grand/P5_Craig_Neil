package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.models.*;
import com.safetynet.safetynetAlerts.utils.Utils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling API operations related to Person java object.
 */
@Setter
@Service
public class PersonAPIService {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    private static final Logger logger = LoggerFactory.getLogger(PersonAPIService.class);

    /**
     * Processes a child alert for a given address.
     *
     * @param address the address to process the child alert for
     * @return a {@link ChildAlertDTO} containing the children and adults at the address
     * @throws Exception if no persons or medical records are found for the address
     */
    public ChildAlertDTO processChildAlert(String address) throws Exception {
        logger.info("Collecting Persons on address");
        List<Person> persons = personDAO.collectOnAddresses(Collections.singletonList(address));
        if (!persons.isEmpty()) {
            List<ChildDTO> children = new ArrayList<>();
            List<PersonIdDTO> adults = new ArrayList<>();
            for (Person person : persons) {
                logger.info("Collecting MedicalRecord of {}", person);
                Optional<MedicalRecord> optionalRecord = medicalRecordDAO.findMedicalRecord(person.getFirstName(),
                        person.getLastName());
                if (optionalRecord.isPresent()) {
                    MedicalRecord medicalRecord = optionalRecord.get();
                    int age = Utils.convertToAge(medicalRecord.getBirthdate());
                    if (age < 18) {
                        ChildDTO child = new ChildDTO();
                        child.setFirstName(person.getFirstName());
                        child.setLastName(person.getLastName());
                        child.setAge(age);
                        children.add(child);
                    } else {
                        PersonIdDTO adult = new PersonIdDTO();
                        adult.setFirstName(person.getFirstName());
                        adult.setLastName(person.getLastName());
                        adults.add(adult);
                    }
                } else {
                    logger.error("Cannot find MedicalRecord of {}", person);
                    throw new Exception("Cannot find MedicalRecord");
                }
            }
            if (children.isEmpty()) {
                return null;
            } else {
                ChildAlertDTO childAlertDTO = new ChildAlertDTO();
                childAlertDTO.setChildren(children);
                childAlertDTO.setAdults(adults);
                return childAlertDTO;
            }
        } else {
            logger.error("Cannot find Person living at this address");
            throw new Exception("Cannot find Person");
        }
    }

    /**
     * Processes person information for a given first name and last name.
     *
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @return a list of {@link PersonInfoDTO} containing the information of persons with the given name
     * @throws Exception if no persons or medical records are found for the given name
     */
    public List<PersonInfoDTO> processPersonInfo(String firstName, String lastName) throws Exception {
        logger.info("Collecting Persons on firstname and lastname");
        List<Person> persons = personDAO.collectPerson(firstName, lastName);
        if (!persons.isEmpty()) {
            List<PersonInfoDTO> personsDTO = new ArrayList<>();
            for (Person person : persons) {
                logger.info("Collecting MedicalRecord of {}", person);
                Optional<MedicalRecord> optionalRecord = medicalRecordDAO.findMedicalRecord(person.getFirstName(),
                        person.getLastName());
                if (optionalRecord.isPresent()) {
                    MedicalRecord medicalRecord = optionalRecord.get();
                    int age = Utils.convertToAge(medicalRecord.getBirthdate());
                    PersonInfoDTO personInfoDTO = new PersonInfoDTO();
                    personInfoDTO.setFirstName(firstName);
                    personInfoDTO.setLastName(lastName);
                    personInfoDTO.setAddress(person.getAddress());
                    personInfoDTO.setCity(person.getCity());
                    personInfoDTO.setZip(person.getZip());
                    personInfoDTO.setAge(age);
                    personInfoDTO.setEmail(person.getEmail());
                    personInfoDTO.setMedications(medicalRecord.getMedications());
                    personInfoDTO.setAllergies(medicalRecord.getAllergies());
                    personsDTO.add(personInfoDTO);
                } else {
                    logger.error("Cannot find MedicalRecord of {}", person);
                    throw new Exception("Cannot find MedicalRecord");
                }
            }
            return personsDTO;
        } else {
            logger.error("Cannot find Person");
            throw new Exception("Cannot find Person");
        }
    }

    /**
     * Processes community emails for a given city.
     *
     * @param city the city to process the emails for
     * @return a list of email addresses of persons in the given city
     * @throws Exception if no persons are found in the city
     */
    public List<String> processCommunityEmail(String city) throws Exception {
        logger.info("Collecting Persons on city");
        List<Person> persons = personDAO.collectOnCity(city);
        if (!persons.isEmpty()) {
            return persons.stream().map(Person::getEmail).toList();
        } else {
            logger.error("Cannot find Person");
            throw new Exception("Cannot find Person");
        }
    }
}

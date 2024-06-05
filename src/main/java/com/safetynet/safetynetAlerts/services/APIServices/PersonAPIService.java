package com.safetynet.safetynetAlerts.services.APIServices;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.models.APIDTOs.ChildAlertDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.ChildDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.PersonInfoDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.utils.Utils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Service
public class PersonAPIService {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    public ChildAlertDTO processChildAlert(String address) throws Exception {
        List<Person> persons = personDAO.collectOnAddresses(Collections.singletonList(address));
        if (!persons.isEmpty()) {
            List<ChildDTO> children = new ArrayList<>();
            List<PersonIdDTO> adults = new ArrayList<>();
            for (Person person : persons) {
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
                    throw new Exception("Cannot find medical record");
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
            throw new Exception("Cannot find person");
        }
    }

    public List<PersonInfoDTO> processPersonInfo(String firstName, String lastName) throws Exception {
        List<Person> persons = personDAO.collectPerson(firstName, lastName);
        if (!persons.isEmpty()) {
            List<PersonInfoDTO> personsDTO = new ArrayList<>();
            for (Person person : persons) {
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
                    throw new Exception("Cannot find medical record");
                }
            }
            return personsDTO;
        } else {
            throw new Exception("Cannot find person");
        }
    }

    public List<String> processCommunityEmail(String city) throws Exception {
        List<Person> persons = personDAO.collectOnCity(city);
        if (!persons.isEmpty()) {
            return persons.stream().map(Person::getEmail).toList();
        } else {
            throw new Exception("Cannot find person");
        }
    }
}

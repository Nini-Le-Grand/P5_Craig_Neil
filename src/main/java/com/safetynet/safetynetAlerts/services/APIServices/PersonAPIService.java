package com.safetynet.safetynetAlerts.services.APIServices;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.APIDTOs.childAlertDTOs.ChildAlertDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.childAlertDTOs.ChildDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.personInfoDTOs.PersonInfoDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PersonAPIService {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    public Object processChildAlert(String address) throws Exception {
        List<Person> homeMembers = personDAO.collectOnAddresses(Collections.singletonList(address));
        if (!homeMembers.isEmpty()) {
            List<ChildDTO> children = new ArrayList<>();
            List<PersonIdDTO> adults = new ArrayList<>();
            for (Person member : homeMembers) {
                int age = medicalRecordDAO.getAge(member.getFirstName(), member.getLastName());
                if (age < 18) {
                    ChildDTO child = new ChildDTO(member.getFirstName(), member.getLastName(), age);
                    children.add(child);
                } else {
                    PersonIdDTO adult = new PersonIdDTO(member.getFirstName(), member.getLastName());
                    adults.add(adult);
                }
            }
            return children.isEmpty() ? "" : new ChildAlertDTO(children, adults);
        } else {
            throw new NotFoundException("Cannot find any persons");
        }
    }

    public List<PersonInfoDTO> processPersonInfo(String firstName, String lastName) throws NotFoundException {
        List<Person> persons = personDAO.collectPerson(firstName, lastName);
        if (!persons.isEmpty()) {
            List<PersonInfoDTO> personsDTO = new ArrayList<>();
            for (Person person : persons) {
                Optional<MedicalRecord> optionalRecord = medicalRecordDAO.findMedicalRecord(person.getFirstName(), person.getLastName());
                if (optionalRecord.isPresent()) {
                    MedicalRecord medicalRecord = optionalRecord.get();
                    PersonInfoDTO personInfoDTO = new PersonInfoDTO(
                            firstName,
                            lastName,
                            person.getAddress(),
                            person.getCity(),
                            person.getZip(),
                            person.getEmail(),
                            medicalRecord.getBirthdate(),
                            medicalRecord.getMedications(),
                            medicalRecord.getAllergies()
                    );
                    personsDTO.add(personInfoDTO);
                } else {
                    throw new NotFoundException("Medical record not found");
                }
            }
            return personsDTO;
        } else {
            throw new NotFoundException("Cannot find anybody");
        }
    }

    public List<String> processCommunityEmail(String city) throws NotFoundException {
        List<Person> persons = personDAO.collectOnCity(city);
        if (!persons.isEmpty()) {
            return persons.stream().map(Person::getEmail).toList();
        } else {
            throw new NotFoundException("Nobody lives here ...");
        }
    }
}

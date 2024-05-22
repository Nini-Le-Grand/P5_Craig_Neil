package com.safetynet.safetynetAlerts.services.APIServices;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.APIDTOs.fireDTOs.FireDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.fireDTOs.PersonFireDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.firestationDTOs.FirestationDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.firestationDTOs.PersonFirestationDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs.AddressDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs.FloodDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs.PersonFloodDTO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FirestationAPIService {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    @Autowired
    private FirestationDAO firestationDAO;

    public FirestationDTO processFirestation(String station) throws Exception {
        List<String> addresses = firestationDAO.collectFirestation(station)
                .stream()
                .map(Firestation::getAddress)
                .toList();

        if (!addresses.isEmpty()) {
            List<Person> persons = personDAO.collectOnAddresses(addresses);
            if (!persons.isEmpty()) {
                List<PersonFirestationDTO> list = new ArrayList<>();
                int children = 0;
                for (Person person : persons) {
                    PersonFirestationDTO personDTO = new PersonFirestationDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
                    list.add(personDTO);
                    int age = medicalRecordDAO.getAge(person.getFirstName(), person.getLastName());
                    if (age < 18) {
                        children += 1;
                    }
                }
                return new FirestationDTO(list, list.size() - children, children);
            } else {
                throw new NotFoundException("Cannot find any persons");
            }
        } else {
            throw new NotFoundException("This station does not serve any address.");
        }
    }

    public List<String> processPhoneAlert(String station) throws NotFoundException {
        List<String> addresses = firestationDAO.collectFirestation(station)
                .stream()
                .map(Firestation::getAddress)
                .toList();

        if (!addresses.isEmpty()) {
            List<Person> persons = personDAO.collectOnAddresses(addresses);
            if (!persons.isEmpty()) {
                List<String> phones = persons.stream().map(Person::getPhone).toList();
                Set<String> uniquePhones = new HashSet<>(phones);
                return new ArrayList<>(uniquePhones);
            } else {
                throw new NotFoundException("Cannot find any person");
            }
        } else {
            throw new NotFoundException("This station does not serve any address.");
        }
    }

    public FireDTO processFire(String address) throws NotFoundException {
        Optional<Firestation> station = firestationDAO.findFirestation(address);
        if (station.isPresent()) {
            List<Person> persons = personDAO.collectOnAddresses(Collections.singletonList(address));
            if (!persons.isEmpty()) {
                FireDTO dto = new FireDTO(new ArrayList<>(), station.get().getStation());
                for (Person person : persons) {
                    Optional<MedicalRecord> record = medicalRecordDAO.findMedicalRecord(person.getFirstName(), person.getLastName());
                    if (record.isPresent()) {
                        MedicalRecord medicalRecord = record.get();
                        PersonFireDTO personDTO = new PersonFireDTO(person.getFirstName(), person.getLastName(), person.getPhone(), medicalRecord.getBirthdate(), medicalRecord.getMedications(), medicalRecord.getAllergies());
                        dto.getPersons().add(personDTO);
                    } else {
                        throw new NotFoundException("Medical record is missing");
                    }
                }
                return dto;
            } else {
                throw new NotFoundException("Cannot find any person");
            }
        } else {
            throw new NotFoundException("Cannot find any firestation");
        }
    }

    public List<FloodDTO> processFlood(String[] stations) throws NotFoundException {
        List<FloodDTO> dto = new ArrayList<>();
        for (String station : stations) {
            FloodDTO floodDto = new FloodDTO(station, new ArrayList<>());
            List<String> addresses = firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();
            if (!addresses.isEmpty()) {
                for (String address : addresses) {
                    AddressDTO addressDto = new AddressDTO(address, new ArrayList<>());
                    List<Person> persons = personDAO.collectOnAddress(address);
                    if (!persons.isEmpty()) {
                        for (Person person : persons) {
                            Optional<MedicalRecord> record = medicalRecordDAO.findMedicalRecord(person.getFirstName(), person.getLastName());
                            if (record.isPresent()) {
                                MedicalRecord medicalRecord = record.get();
                                PersonFloodDTO personFloodDto = new PersonFloodDTO(
                                        person.getFirstName(),
                                        person.getLastName(),
                                        person.getPhone(),
                                        medicalRecord.getBirthdate(),
                                        medicalRecord.getMedications(),
                                        medicalRecord.getAllergies()
                                );
                                addressDto.getPersons().add(personFloodDto);
                            } else {
                                throw new NotFoundException("Cannot find medical record");
                            }
                        }
                        floodDto.getAddresses().add(addressDto);
                    } else {
                        throw new NotFoundException("Cannot find person");
                    }
                }
                dto.add(floodDto);
            }
        }
        if (!dto.isEmpty()) {
            return dto;
        } else {
            throw new NotFoundException("Cannot find firestation");
        }
    }
}

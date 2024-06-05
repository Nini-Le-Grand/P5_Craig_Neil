package com.safetynet.safetynetAlerts.services.APIServices;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.models.APIDTOs.FireDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.PersonFireDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.FirestationDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.PersonFirestationDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.AddressDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.FloodDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.PersonFloodDTO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.utils.Utils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Setter
@Service
public class FirestationAPIService {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    @Autowired
    private FirestationDAO firestationDAO;

    public FirestationDTO processFirestation(String station) throws Exception {
        List<String> addresses =
                firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();

        if (!addresses.isEmpty()) {
            List<Person> persons = personDAO.collectOnAddresses(addresses);
            if (!persons.isEmpty()) {
                List<PersonFirestationDTO> personFirestationDTOS = new ArrayList<>();
                int children = 0;
                for (Person person : persons) {
                    PersonFirestationDTO personDTO = new PersonFirestationDTO();
                    personDTO.setFirstName(person.getFirstName());
                    personDTO.setLastName(person.getLastName());
                    personDTO.setAddress(person.getAddress());
                    personDTO.setCity(person.getCity());
                    personDTO.setZip(person.getZip());
                    personDTO.setPhone(person.getPhone());

                    personFirestationDTOS.add(personDTO);

                    Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(person.getFirstName(),
                            person.getLastName());
                    if (medicalRecord.isPresent()) {
                        String birthdate = medicalRecord.get().getBirthdate();
                        int age = Utils.convertToAge(birthdate);
                        if (age < 18) {
                            children += 1;
                        }
                    } else {
                        throw new Exception("Cannot find medical record");
                    }
                }
                FirestationDTO firestationDTO = new FirestationDTO();
                firestationDTO.setPersons(personFirestationDTOS);
                firestationDTO.setAdults(personFirestationDTOS.size() - children);
                firestationDTO.setChildren(children);
                return firestationDTO;
            } else {
                throw new Exception("Cannot find person");
            }
        } else {
            throw new Exception("Cannot find firestation");
        }
    }

    public List<String> processPhoneAlert(String station) throws Exception {
        List<String> addresses =
                firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();

        if (!addresses.isEmpty()) {
            List<Person> persons = personDAO.collectOnAddresses(addresses);
            if (!persons.isEmpty()) {
                List<String> phones = persons.stream().map(Person::getPhone).toList();
                Set<String> uniquePhones = new HashSet<>(phones);
                return new ArrayList<>(uniquePhones);
            } else {
                throw new Exception("Cannot find person");
            }
        } else {
            throw new Exception("Cannot find firestation");
        }
    }

    public FireDTO processFire(String address) throws Exception {
        Optional<Firestation> station = firestationDAO.findFirestation(address);
        if (station.isPresent()) {
            List<Person> persons = personDAO.collectOnAddresses(Collections.singletonList(address));
            if (!persons.isEmpty()) {
                FireDTO dto = new FireDTO();
                dto.setPersons(new ArrayList<>());
                dto.setStation(station.get().getStation());
                for (Person person : persons) {
                    Optional<MedicalRecord> record = medicalRecordDAO.findMedicalRecord(person.getFirstName(),
                            person.getLastName());
                    if (record.isPresent()) {
                        MedicalRecord medicalRecord = record.get();
                        int age = Utils.convertToAge(medicalRecord.getBirthdate());
                        PersonFireDTO personDTO = new PersonFireDTO();
                        personDTO.setFirstName(person.getFirstName());
                        personDTO.setLastName(person.getLastName());
                        personDTO.setPhone(person.getPhone());
                        personDTO.setAge(age);
                        personDTO.setMedications(medicalRecord.getMedications());
                        personDTO.setAllergies(medicalRecord.getAllergies());
                        dto.getPersons().add(personDTO);
                    } else {
                        throw new Exception("Cannot find medical record");
                    }
                }
                return dto;
            } else {
                throw new Exception("Cannot find person");
            }
        } else {
            throw new Exception("Cannot find firestation");
        }
    }

    public List<FloodDTO> processFlood(String[] stations) throws Exception {
        List<FloodDTO> dto = new ArrayList<>();
        for (String station : stations) {
            FloodDTO floodDto = new FloodDTO();
            floodDto.setStation(station);
            floodDto.setAddresses(new ArrayList<>());
            List<String> addresses =
                    firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();

            if (!addresses.isEmpty()) {
                for (String address : addresses) {
                    AddressDTO addressDto = new AddressDTO();
                    addressDto.setAddress(address);
                    addressDto.setPersons(new ArrayList<>());
                    List<Person> persons = personDAO.collectOnAddress(address);
                    if (!persons.isEmpty()) {
                        for (Person person : persons) {
                            Optional<MedicalRecord> record = medicalRecordDAO.findMedicalRecord(person.getFirstName()
                                    , person.getLastName());
                            if (record.isPresent()) {
                                MedicalRecord medicalRecord = record.get();
                                int age = Utils.convertToAge(medicalRecord.getBirthdate());
                                PersonFloodDTO personFloodDto = new PersonFloodDTO();
                                personFloodDto.setFirstName(person.getFirstName());
                                personFloodDto.setLastName(person.getLastName());
                                personFloodDto.setPhone(person.getPhone());
                                personFloodDto.setAge(age);
                                personFloodDto.setMedications(medicalRecord.getMedications());
                                personFloodDto.setAllergies(medicalRecord.getAllergies());
                                addressDto.getPersons().add(personFloodDto);
                            } else {
                                throw new Exception("Cannot find medical record");
                            }
                        }
                        floodDto.getAddresses().add(addressDto);
                    } else {
                        throw new Exception("Cannot find person");
                    }
                }
                dto.add(floodDto);
            }
        }
        if (!dto.isEmpty()) {
            return dto;
        } else {
            throw new Exception("Cannot find firestation");
        }
    }
}

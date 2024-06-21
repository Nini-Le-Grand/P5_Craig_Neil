package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.DAO.PersonDAO;
import com.safetynet.safetynetAlerts.models.FireDTO;
import com.safetynet.safetynetAlerts.models.PersonFireDTO;
import com.safetynet.safetynetAlerts.models.FirestationDTO;
import com.safetynet.safetynetAlerts.models.PersonFirestationDTO;
import com.safetynet.safetynetAlerts.models.AddressDTO;
import com.safetynet.safetynetAlerts.models.FloodDTO;
import com.safetynet.safetynetAlerts.models.PersonFloodDTO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.utils.Utils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for handling API operations related to Firestation java objects.
 */
@Setter
@Service
public class FirestationAPIService {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    @Autowired
    private FirestationDAO firestationDAO;

    private static final Logger logger = LoggerFactory.getLogger(FirestationAPIService.class);

    /**
     * Processes firestation information for a given station number.
     *
     * @param station the station number to process the information for
     * @return a {@link FirestationDTO} containing persons covered by the station, including count of adults and children
     * @throws Exception if no firestations, persons, or medical records are found for the station
     */
    public FirestationDTO processFirestation(String station) throws Exception {
        logger.info("Collecting addresses on station");
        List<String> addresses =
                firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();
        if (!addresses.isEmpty()) {
            logger.info("Collecting Person on addresses");
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

                    logger.info("Collecting MedicalRecord of {}", person);
                    Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(person.getFirstName(),
                            person.getLastName());
                    if (medicalRecord.isPresent()) {
                        String birthdate = medicalRecord.get().getBirthdate();
                        int age = Utils.convertToAge(birthdate);
                        if (age < 18) {
                            children += 1;
                        }
                    } else {
                        logger.error("Cannot find MedicalRecord of {}", person);
                        throw new Exception("Cannot find MedicalRecord");
                    }
                }
                FirestationDTO firestationDTO = new FirestationDTO();
                firestationDTO.setPersons(personFirestationDTOS);
                firestationDTO.setAdults(personFirestationDTOS.size() - children);
                firestationDTO.setChildren(children);
                return firestationDTO;
            } else {
                logger.error("Cannot find Person at these addresses");
                throw new Exception("Cannot find Person");
            }
        } else {
            logger.error("Cannot find Firestation");
            throw new Exception("Cannot find Firestation");
        }
    }

    /**
     * Processes phone alerts for a given station number.
     *
     * @param station the station number to process the phone alerts for
     * @return a list of unique phone numbers of persons covered by the station
     * @throws Exception if no firestations or persons are found for the station
     */
    public List<String> processPhoneAlert(String station) throws Exception {
        logger.info("Collecting addresses on station");
        List<String> addresses =
                firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();

        if (!addresses.isEmpty()) {
            logger.info("Collecting Person on addresses");
            List<Person> persons = personDAO.collectOnAddresses(addresses);
            if (!persons.isEmpty()) {
                List<String> phones = persons.stream().map(Person::getPhone).toList();
                Set<String> uniquePhones = new HashSet<>(phones);
                return new ArrayList<>(uniquePhones);
            } else {
                logger.error("Cannot find Person at these addresses");
                throw new Exception("Cannot find Person");
            }
        } else {
            logger.error("Cannot find Firestation");
            throw new Exception("Cannot find Firestation");
        }
    }

    /**
     * Processes fire information for a given address.
     *
     * @param address the address to process the fire information for
     * @return a {@link FireDTO} containing persons at the address and the station serving it
     * @throws Exception if no firestation, persons, or medical records are found for the address
     */
    public FireDTO processFire(String address) throws Exception {
        logger.info("Collecting station on address");
        Optional<Firestation> station = firestationDAO.findFirestation(address);
        if (station.isPresent()) {
            logger.info("Collecting Persons on station");
            List<Person> persons = personDAO.collectOnAddresses(Collections.singletonList(address));
            if (!persons.isEmpty()) {
                FireDTO fireDTO = new FireDTO();
                fireDTO.setPersons(new ArrayList<>());
                fireDTO.setStation(station.get().getStation());
                for (Person person : persons) {
                    logger.info("Collecting MedicalRecord of {}", person);
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
                        fireDTO.getPersons().add(personDTO);
                    } else {
                        logger.error("Cannot find MedicalRecord of {}", person);
                        throw new Exception("Cannot find MedicalRecord");
                    }
                }
                return fireDTO;
            } else {
                logger.error("Cannot find Person");
                throw new Exception("Cannot find Person");
            }
        } else {
            logger.error("Cannot find station");
            throw new Exception("Cannot find Firestation");
        }
    }

    /**
     * Processes flood information for given station numbers.
     *
     * @param stations an array of station numbers to process the flood information for
     * @return a list of {@link FloodDTO} containing addresses and persons covered by the stations
     * @throws Exception if no firestations, persons, or medical records are found for the stations
     */
    public List<FloodDTO> processFlood(String[] stations) throws Exception {
        List<FloodDTO> dto = new ArrayList<>();
        for (String station : stations) {
            logger.info("Collecting addresses of {}", station);
            FloodDTO floodDto = new FloodDTO();
            floodDto.setStation(station);
            floodDto.setAddresses(new ArrayList<>());
            List<String> addresses =
                    firestationDAO.collectFirestation(station).stream().map(Firestation::getAddress).toList();

            if (!addresses.isEmpty()) {
                for (String address : addresses) {
                    logger.info("Collecting Persons of {}", address);
                    AddressDTO addressDto = new AddressDTO();
                    addressDto.setAddress(address);
                    addressDto.setPersons(new ArrayList<>());
                    List<Person> persons = personDAO.collectOnAddress(address);
                    if (!persons.isEmpty()) {
                        for (Person person : persons) {
                            logger.info("Collecting MedicalRecord of {}", person);
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
                                logger.error("Cannot find MedicalRecord of {}", person);
                                throw new Exception("Cannot find MedicalRecord");
                            }
                        }
                        floodDto.getAddresses().add(addressDto);
                    } else {
                        logger.error("Cannot find Person of {}", address);
                        throw new Exception("Cannot find Person");
                    }
                }
                dto.add(floodDto);
            }
        }
        if (!dto.isEmpty()) {
            return dto;
        } else {
            logger.error("Cannot find station");
            throw new Exception("Cannot find Firestation");
        }
    }
}

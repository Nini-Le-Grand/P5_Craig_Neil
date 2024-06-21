package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling operations related to {@link MedicalRecord} java object.
 */
@Setter
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    /**
     * Saves a new {@link MedicalRecord} entity to the data set.
     *
     * @param medicalRecord the MedicalRecord entity to save
     * @throws Exception if the medical record already exists in the data set
     */
    public void saveMedicalRecord(MedicalRecord medicalRecord) throws Exception {
        logger.info("Checking if MedicalRecord exists in data set");
        if (medicalRecordDAO.findMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName()).isEmpty()) {
            medicalRecordDAO.addMedicalRecord(medicalRecord);
        } else {
            logger.error("MedicalRecord already exists in data set");
            throw new Exception("MedicalRecord already saved");
        }
    }

    /**
     * Edits an existing {@link MedicalRecord} entity in the data set.
     *
     * @param medicalRecordDTO the MedicalRecordUpdateDTO containing updated information
     * @throws Exception if the medical record does not exist in the dataset
     */
    public void editMedicalRecord(MedicalRecordUpdateDTO medicalRecordDTO) throws Exception {
        logger.info("Checking if MedicalRecord exists in data set");
        Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(medicalRecordDTO.getFirstName(),
                medicalRecordDTO.getLastName());
        if (medicalRecord.isPresent()) {
            medicalRecordDAO.setMedicalRecords(medicalRecordDTO, medicalRecord.get());
        } else {
            logger.error("Cannot find MedicalRecord in data set");
            throw new Exception("Cannot find MedicalRecord to edit");
        }
    }

    /**
     * Deletes an existing {@link MedicalRecord} entity from the data set.
     *
     * @param idPersonDTO the PersonIdDTO containing the ID information of the person whose medical record is to be deleted
     * @throws Exception if the medical record does not exist in the data set
     */
    public void deleteMedicalRecord(PersonIdDTO idPersonDTO) throws Exception {
        logger.info("Checking if MedicalRecord exists in data set");
        Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(idPersonDTO.getFirstName(),
                idPersonDTO.getLastName());
        if (medicalRecord.isPresent()) {
            medicalRecordDAO.removeMedicalRecord(medicalRecord.get());
        } else {
            logger.error("Cannot find MedicalRecord in data set");
            throw new Exception("Cannot find MedicalRecord to delete");
        }
    }
}

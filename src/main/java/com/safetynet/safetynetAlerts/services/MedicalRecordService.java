package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import com.safetynet.safetynetAlerts.exceptions.ConflictException;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    public void saveMedicalRecord(MedicalRecord medicalRecord) throws ConflictException {
        if (medicalRecordDAO.findMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName()).isEmpty()) {
            medicalRecordDAO.addMedicalRecord(medicalRecord);
        } else {
            throw new ConflictException("MedicalRecord already saved in db");
        }
    }

    public void editMedicalRecord(MedicalRecordUpdateDTO medicalRecordDTO) throws NotFoundException {
        Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(medicalRecordDTO.getFirstName(), medicalRecordDTO.getLastName());
        if (medicalRecord.isPresent()) {
            medicalRecordDAO.setMedicalRecords(medicalRecordDTO, medicalRecord.get());
        } else {
            throw new NotFoundException("Cannot find medical record to update");
        }
    }

    public void deleteMedicalRecord(PersonIdDTO idPersonDTO) throws NotFoundException {
        Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(idPersonDTO.getFirstName(), idPersonDTO.getLastName());
        if (medicalRecord.isPresent()) {
            medicalRecordDAO.removeMedicalRecord(medicalRecord.get());
        } else {
            throw new NotFoundException("Cannot find medical record to delete");
        }
    }
}

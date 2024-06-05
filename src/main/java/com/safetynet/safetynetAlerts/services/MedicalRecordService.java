package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.MedicalRecordDAO;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    public void saveMedicalRecord(MedicalRecord medicalRecord) throws Exception {
        if (medicalRecordDAO.findMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName()).isEmpty()) {
            medicalRecordDAO.addMedicalRecord(medicalRecord);
        } else {
            throw new Exception("MedicalRecord already saved");
        }
    }

    public void editMedicalRecord(MedicalRecordUpdateDTO medicalRecordDTO) throws Exception {
        Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(medicalRecordDTO.getFirstName(),
                medicalRecordDTO.getLastName());
        if (medicalRecord.isPresent()) {
            medicalRecordDAO.setMedicalRecords(medicalRecordDTO, medicalRecord.get());
        } else {
            throw new Exception("Cannot find medical record to edit");
        }
    }

    public void deleteMedicalRecord(PersonIdDTO idPersonDTO) throws Exception {
        Optional<MedicalRecord> medicalRecord = medicalRecordDAO.findMedicalRecord(idPersonDTO.getFirstName(),
                idPersonDTO.getLastName());
        if (medicalRecord.isPresent()) {
            medicalRecordDAO.removeMedicalRecord(medicalRecord.get());
        } else {
            throw new Exception("Cannot find medical record to delete");
        }
    }
}

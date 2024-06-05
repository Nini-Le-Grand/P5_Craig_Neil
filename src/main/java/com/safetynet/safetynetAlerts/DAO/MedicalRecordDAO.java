package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordDAO {
    @Autowired
    private JSONDataDAO jsonDataDAO;

    public List<MedicalRecord> getMedicalRecords() {
        return jsonDataDAO.getJsonData().getMedicalrecords();
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        getMedicalRecords().add(medicalRecord);
    }

    public void setMedicalRecords(MedicalRecordUpdateDTO medicalRecord, MedicalRecord medicalRecordToEdit) {
        if (!medicalRecord.getBirthdate().isEmpty()) medicalRecordToEdit.setBirthdate(medicalRecord.getBirthdate());
        if (medicalRecord.getMedications() != null) medicalRecordToEdit.setMedications(medicalRecord.getMedications());
        if (medicalRecord.getAllergies() != null) medicalRecordToEdit.setAllergies(medicalRecord.getAllergies());
    }

    public void removeMedicalRecord(MedicalRecord medicalRecord) {
        getMedicalRecords().remove(medicalRecord);
    }

    public Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
        return getMedicalRecords().stream().filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)).findAny();
    }
}

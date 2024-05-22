package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.JSONData;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
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
        if (!Objects.equals(medicalRecord.getBirthdate(), ""))
            medicalRecordToEdit.setBirthdate(medicalRecord.getBirthdate());
        if (medicalRecord.getMedications() != null) medicalRecordToEdit.setMedications(medicalRecord.getMedications());
        if (medicalRecord.getAllergies() != null) medicalRecordToEdit.setAllergies(medicalRecord.getAllergies());
    }

    public void removeMedicalRecord(MedicalRecord medicalRecord) {
        getMedicalRecords().remove(medicalRecord);
    }

    public Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
        return getMedicalRecords()
                .stream()
                .filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName))
                .findAny();
    }

    public int getAge(String firstName, String lastName) throws Exception {
        Optional<MedicalRecord> record = findMedicalRecord(firstName, lastName);
        if (record.isPresent()) {
            try {
                String birthdateString = record.get().getBirthdate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthdate = LocalDate.parse(birthdateString, formatter);
                LocalDate currentDate = LocalDate.now();
                return Period.between(birthdate, currentDate).getYears();
            } catch (Exception e) {
                throw new Exception("Invalid date format ! Valid : 'MM/dd/yyyy");
            }
        } else {
            throw new NotFoundException("Medical record is missing");
        }
    }

}

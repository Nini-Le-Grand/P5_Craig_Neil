package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.data.JSONDataLoader;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing {@link MedicalRecord} java object.
 */
@Service
public class MedicalRecordDAO {
    @Autowired
    private JSONDataLoader jsonDataLoader;

    /**
     * Retrieves all {@link MedicalRecord} from the data set.
     *
     * @return a list of all medical records.
     */
    public List<MedicalRecord> getMedicalRecords() {
        return jsonDataLoader.getJsonData().getMedicalrecords();
    }

    /**
     * Adds a new {@link MedicalRecord} to the data set.
     *
     * @param medicalRecord the medical record to be added.
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        getMedicalRecords().add(medicalRecord);
    }

    /**
     * Updates the details of an existing {@link MedicalRecord}.
     *
     * @param medicalRecord the updated medical record details.
     * @param medicalRecordToEdit the medical record to be edited.
     */
    public void setMedicalRecords(MedicalRecordUpdateDTO medicalRecord, MedicalRecord medicalRecordToEdit) {
        if (!medicalRecord.getBirthdate().isEmpty()) medicalRecordToEdit.setBirthdate(medicalRecord.getBirthdate());
        if (medicalRecord.getMedications() != null) medicalRecordToEdit.setMedications(medicalRecord.getMedications());
        if (medicalRecord.getAllergies() != null) medicalRecordToEdit.setAllergies(medicalRecord.getAllergies());
    }

    /**
     * Removes a {@link MedicalRecord} from the data set.
     *
     * @param medicalRecord the medical record to be removed.
     */
    public void removeMedicalRecord(MedicalRecord medicalRecord) {
        getMedicalRecords().remove(medicalRecord);
    }

    /**
     * Finds a {@link MedicalRecord} by the first and last name of the person.
     *
     * @param firstName the first name of the person.
     * @param lastName the last name of the person.
     * @return an {@link Optional} containing the medical record if found, otherwise empty.
     */
    public Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
        return getMedicalRecords().stream().filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)).findAny();
    }
}

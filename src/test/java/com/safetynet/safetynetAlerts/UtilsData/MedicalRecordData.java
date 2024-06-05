package com.safetynet.safetynetAlerts.UtilsData;

import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordData {
    public MedicalRecord getMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        List<String> medications = new ArrayList<>();
        List<String> allergies = new ArrayList<>();

        medications.add("doliprane:500mg");
        allergies.add("pollen");

        medicalRecord.setFirstName("Neil");
        medicalRecord.setLastName("Craig");
        medicalRecord.setBirthdate("03/20/1992");
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);

        return medicalRecord;
    }

    public MedicalRecordUpdateDTO getMedicalRecordUpdateDTO() {
        MedicalRecordUpdateDTO medicalRecord = new MedicalRecordUpdateDTO();

        medicalRecord.setFirstName("Neil");
        medicalRecord.setLastName("Craig");
        medicalRecord.setBirthdate("03/20/2010");
        medicalRecord.setMedications(null);
        medicalRecord.setAllergies(null);

        return medicalRecord;
    }

    public List<MedicalRecord> getListOfMedicalRecord() {
        List<String> medications1 = new ArrayList<>();
        medications1.add("Doliprane: 500mg");
        medications1.add("Cortizon: 250mg");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("Peanuts");
        allergies1.add("Shellfish");

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("Neil");
        medicalRecord1.setLastName("Craig");
        medicalRecord1.setBirthdate("03/20/1992");
        medicalRecord1.setMedications(medications1);
        medicalRecord1.setAllergies(allergies1);

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Laura");
        medicalRecord2.setLastName("Delvine");
        medicalRecord2.setBirthdate("01/01/2010");

        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(medicalRecord1);
        medicalRecords.add(medicalRecord2);

        return medicalRecords;
    }
}

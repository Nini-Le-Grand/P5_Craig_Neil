package com.safetynet.safetynetAlerts.models;

import lombok.Data;

import java.util.List;

@Data
public class JSONData {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;
}

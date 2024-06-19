package com.safetynet.safetynetAlerts.models;

import lombok.Data;

import java.util.List;

/**
 * Represents JSON data containing lists of persons, fire stations, and medical records.
 */
@Data
public class JSONData {

    private List<Person> persons;

    private List<Firestation> firestations;

    private List<MedicalRecord> medicalrecords;
}

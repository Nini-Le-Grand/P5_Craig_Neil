package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a medical record of a person, including personal details, birthdate, medications, and allergies.
 */
@Data
public class MedicalRecord {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String birthdate;

    @NotNull
    private List<String> medications;

    @NotNull
    private List<String> allergies;
}

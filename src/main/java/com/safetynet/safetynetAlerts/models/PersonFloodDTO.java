package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object gathering information about a person affected by a flood.
 */
@Data
public class PersonFloodDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private int age;

    @NotNull
    private List<String> medications;

    @NotNull
    private List<String> allergies;
}

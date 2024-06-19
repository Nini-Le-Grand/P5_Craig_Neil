package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object gathering detailed information about a person.
 */
@Data
public class PersonInfoDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String zip;

    @NotBlank
    private String email;

    @NotBlank
    private int age;

    @NotNull
    private List<String> medications;

    @NotNull
    private List<String> allergies;
}

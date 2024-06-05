package com.safetynet.safetynetAlerts.models.APIDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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

package com.safetynet.safetynetAlerts.models.APIDTOs.personInfoDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonInfoDTO {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private String zip;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String birthdate;

    @NotNull
    private List<String> medications;

    @NotNull
    private List<String> allergies;
}

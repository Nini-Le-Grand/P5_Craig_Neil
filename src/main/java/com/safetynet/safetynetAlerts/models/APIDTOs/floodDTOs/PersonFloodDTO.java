package com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonFloodDTO {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String phone;

    @NotNull
    @NotBlank
    private String birthdate;

    @NotNull
    private List<String> medications;

    @NotNull
    private List<String> allergies;
}

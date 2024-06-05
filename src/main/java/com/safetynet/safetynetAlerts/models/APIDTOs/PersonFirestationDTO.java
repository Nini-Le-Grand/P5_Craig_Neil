package com.safetynet.safetynetAlerts.models.APIDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonFirestationDTO {

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
    private String phone;
}

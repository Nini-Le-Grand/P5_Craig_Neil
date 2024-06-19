package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Represents a data transfer object gathering information about a person associated with a fire station.
 */
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

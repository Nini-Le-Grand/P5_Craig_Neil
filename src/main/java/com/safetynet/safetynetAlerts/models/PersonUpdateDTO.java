package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents a data transfer object for updating person details.
 */
@Data
public class PersonUpdateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private String address;

    @NotNull
    private String city;

    @NotNull
    private String zip;

    @NotNull
    private String phone;

    @NotNull
    @Email
    private String email;
}

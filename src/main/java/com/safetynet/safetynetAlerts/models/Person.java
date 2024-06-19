package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Represents a person with their personal details.
 */
@Data
public class Person {

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

    @NotBlank
    @Email
    private String email;
}


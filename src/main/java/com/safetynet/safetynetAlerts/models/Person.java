package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Person {
    @NotBlank(message = "firstName must not be blank")
    private String firstName;
    @NotBlank(message = "lastName must not be blank")
    private String lastName;
    @NotBlank(message = "address must not be blank")
    private String address;
    @NotBlank(message = "city must not be blank")
    private String city;
    @NotBlank(message = "zip must not be blank")
    private String zip;
    @NotBlank(message = "phone must not be blank")
    private String phone;
    @NotBlank(message = "email must not be blank")
    @Email
    private String email;
}


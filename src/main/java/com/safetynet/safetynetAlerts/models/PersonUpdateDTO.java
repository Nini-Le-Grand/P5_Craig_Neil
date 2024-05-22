package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonUpdateDTO {
    @NotBlank(message = "firstName must not be blank")
    private String firstName;
    @NotBlank(message = "lastName must not be blank")
    private String lastName;
    @NotNull(message = "address must not be null")
    private String address;
    @NotNull(message = "city must not be null")
    private String city;
    @NotNull(message = "zip must not be null")
    private String zip;
    @NotNull(message = "phone must not be null")
    private String phone;
    @NotNull(message = "email must not be null")
    @Email
    private String email;
}

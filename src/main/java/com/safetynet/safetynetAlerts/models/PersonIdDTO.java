package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonIdDTO {
    @NotBlank(message = "firstName must not be blank")
    private String firstName;
    @NotBlank(message = "lastName must not be blank")
    private String lastName;
}

package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PersonIdDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}

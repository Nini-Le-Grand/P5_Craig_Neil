package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a data transfer object for identifying a person by their first name and last name.
 */
@Data
public class PersonIdDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}

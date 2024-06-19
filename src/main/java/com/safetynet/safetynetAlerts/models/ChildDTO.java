package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Represents a data transfer object for conveying information about children.
 */
@Data
public class ChildDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private int age;
}

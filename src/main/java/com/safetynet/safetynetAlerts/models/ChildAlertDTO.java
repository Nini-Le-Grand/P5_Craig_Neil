package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object for conveying information about children and adults in an alert.
 */
@Data
public class ChildAlertDTO {

    @NotEmpty
    private List<ChildDTO> children;

    @NotEmpty
    private List<PersonIdDTO> adults;
}

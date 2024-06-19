package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object for conveying information about persons affected by a fire at a specific fire station.
 */
@Data
public class FireDTO {

    @NotNull
    private List<PersonFireDTO> persons;

    @NotBlank
    private String station;
}

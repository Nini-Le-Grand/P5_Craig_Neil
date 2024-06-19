package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object for conveying information about persons associated with a fire station.
 */
@Data
public class FirestationDTO {

    @NotNull
    private List<PersonFirestationDTO> persons;

    @NotNull
    private int adults;

    @NotNull
    private int children;
}

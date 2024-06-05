package com.safetynet.safetynetAlerts.models.APIDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FirestationDTO {

    @NotNull
    private List<PersonFirestationDTO> persons;

    @NotNull
    private int adults;

    @NotNull
    private int children;
}

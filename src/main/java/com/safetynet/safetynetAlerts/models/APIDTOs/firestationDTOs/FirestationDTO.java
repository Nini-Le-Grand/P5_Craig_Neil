package com.safetynet.safetynetAlerts.models.APIDTOs.firestationDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FirestationDTO {

    @NotNull
    private List<PersonFirestationDTO> persons;

    @NotNull
    private int adults;

    @NotNull
    private int children;
}

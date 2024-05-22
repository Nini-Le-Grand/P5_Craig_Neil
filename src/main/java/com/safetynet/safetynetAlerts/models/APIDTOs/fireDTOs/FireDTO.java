package com.safetynet.safetynetAlerts.models.APIDTOs.fireDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireDTO {

    @NotNull
    private List<PersonFireDTO> persons;

    @NotNull
    @NotBlank
    private String station;
}

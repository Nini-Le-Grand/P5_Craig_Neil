package com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddressDTO {

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    private List<PersonFloodDTO> persons;
}

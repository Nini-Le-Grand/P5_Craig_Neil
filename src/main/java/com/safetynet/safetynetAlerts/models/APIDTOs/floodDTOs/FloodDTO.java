package com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloodDTO {

    @NotNull
    @NotBlank
    private String station;

    @NotNull
    private List<AddressDTO> addresses;
}

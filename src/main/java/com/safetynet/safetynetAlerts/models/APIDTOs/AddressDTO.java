package com.safetynet.safetynetAlerts.models.APIDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddressDTO {

    @NotBlank
    private String address;

    @NotNull
    private List<PersonFloodDTO> persons;
}

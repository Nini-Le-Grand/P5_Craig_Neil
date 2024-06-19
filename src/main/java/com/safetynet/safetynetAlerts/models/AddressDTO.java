package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object for conveying information about an address and persons affected by a flood.
 */
@Data
public class AddressDTO {

    @NotBlank
    private String address;

    @NotNull
    private List<PersonFloodDTO> persons;
}

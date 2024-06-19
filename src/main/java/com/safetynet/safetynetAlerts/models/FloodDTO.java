package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Represents a data transfer object for conveying information about addresses affected by a flood.
 */
@Data
public class FloodDTO {

    @NotBlank
    private String station;

    @NotNull
    private List<AddressDTO> addresses;
}

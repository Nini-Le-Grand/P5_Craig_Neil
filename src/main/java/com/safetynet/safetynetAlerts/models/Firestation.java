package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Represents a fire station with its address and station number.
 */
@Data
public class Firestation {

    @NotBlank
    private String address;

    @NotBlank
    private String station;
}



package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Firestation {

    @NotBlank
    private String address;

    @NotBlank
    private String station;
}



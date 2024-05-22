package com.safetynet.safetynetAlerts.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Firestation {
    @NotBlank
    private String address;
    @NotBlank
    private String station;
}



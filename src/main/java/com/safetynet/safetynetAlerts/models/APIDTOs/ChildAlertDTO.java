package com.safetynet.safetynetAlerts.models.APIDTOs;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ChildAlertDTO {

    @NotEmpty
    private List<ChildDTO> children;

    @NotEmpty
    private List<PersonIdDTO> adults;
}

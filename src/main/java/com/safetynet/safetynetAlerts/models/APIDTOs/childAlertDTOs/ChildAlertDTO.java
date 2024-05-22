package com.safetynet.safetynetAlerts.models.APIDTOs.childAlertDTOs;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChildAlertDTO {

    @NotNull
    @NotEmpty
    private List<ChildDTO> children;

    @NotNull
    @NotEmpty
    private List<PersonIdDTO> adults;

}

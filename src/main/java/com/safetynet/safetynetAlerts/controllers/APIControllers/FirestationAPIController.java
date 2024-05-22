package com.safetynet.safetynetAlerts.controllers.APIControllers;

import com.safetynet.safetynetAlerts.models.APIDTOs.fireDTOs.FireDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.firestationDTOs.FirestationDTO;
import com.safetynet.safetynetAlerts.models.APIDTOs.floodDTOs.FloodDTO;
import com.safetynet.safetynetAlerts.services.APIServices.FirestationAPIService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class FirestationAPIController {
    @Autowired
    private FirestationAPIService firestationAPIService;

    @GetMapping("/firestation")
    public ResponseEntity<?> getFirestation(@RequestParam("stationNumber") @NotBlank String station) {
        try {
            FirestationDTO dto = firestationAPIService.processFirestation(station);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            ResponseEntity<String> notFound = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            ResponseEntity<String> badRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            return e.getClass().getSimpleName().equals("Exception") ? badRequest : notFound;
        }
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<?> getPhoneAlert(@RequestParam("firestation") @NotBlank String station) {
        try {
            List<String> phones = firestationAPIService.processPhoneAlert(station);
            return ResponseEntity.status(HttpStatus.OK).body(phones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/fire")
    public ResponseEntity<?> getFire(@RequestParam("address") @NotBlank String address) {
        try {
            FireDTO dto = firestationAPIService.processFire(address);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<?> getFlood(@RequestParam("stations") @NotNull @NotEmpty String[] stations) {
        try {
            List<FloodDTO> dto = firestationAPIService.processFlood(stations);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

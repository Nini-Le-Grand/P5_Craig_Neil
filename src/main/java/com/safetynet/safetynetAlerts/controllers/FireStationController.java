package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.services.FirestationService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class FireStationController {
    @Autowired
    private FirestationService firestationService;

    @PostMapping("/firestation")
    public ResponseEntity<String> handlePostFirestation(@RequestBody @Valid Firestation firestation, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                firestationService.saveFirestation(firestation);
                return ResponseEntity.status(HttpStatus.CREATED).body("Firestation successfully saved");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PutMapping("/firestation")
    public ResponseEntity<String> handlePutFirestation(@RequestBody @Valid Firestation firestation, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                firestationService.editFirestation(firestation);
                return ResponseEntity.ok().body("firestation successfully edited");
            } catch (NotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @DeleteMapping("/firestation/{addressOrStation}")
    public ResponseEntity<String> handleDeleteFirestation(@PathVariable @NotBlank String addressOrStation) {
        try {
            firestationService.deleteFirestation(addressOrStation);
            return ResponseEntity.ok().body("firestation successfully deleted");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

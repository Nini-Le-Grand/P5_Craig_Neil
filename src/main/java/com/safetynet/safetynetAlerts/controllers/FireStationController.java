package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.services.FirestationService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class FireStationController {
    @Autowired
    private FirestationService firestationService;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @PostMapping("/firestation")
    public ResponseEntity<String> handlePostFirestation(@RequestBody @Valid Firestation firestation,
                                                        BindingResult result) {
        logger.info("Entered handlePostFirestation method");
        logger.info("Request to save firestation: {}", firestation);
        try {
            firestationService.saveFirestation(firestation);
            logger.info("Firestation successfully saved: {}", firestation);
            logger.info("Exiting handlePostFirestation method");
            return ResponseEntity.status(HttpStatus.CREATED).body("Firestation successfully saved");
        } catch (Exception e) {
            logger.error("Error occurred while saving firestation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/firestation")
    public ResponseEntity<String> handlePutFirestation(@RequestBody @Valid Firestation firestation,
                                                       BindingResult result) {
        logger.info("Entered handlePutFirestation method");
        logger.info("Request to edit firestation: {}", firestation);
        try {
            firestationService.editFirestation(firestation);
            logger.info("Firestation successfully edited: {}", firestation);
            logger.info("Exiting handlePutFirestation method");
            return ResponseEntity.ok().body("Firestation successfully edited");
        } catch (Exception e) {
            logger.error("Error occurred while editing firestation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/firestation/{addressOrStation}")
    public ResponseEntity<String> handleDeleteFirestation(@PathVariable @NotBlank String addressOrStation) {
        logger.info("Entered handleDeleteFirestation method");
        logger.info("Request to delete firestation: {}", addressOrStation);
        try {
            firestationService.deleteFirestation(addressOrStation);
            logger.info("Firestation successfully deleted: {}", addressOrStation);
            logger.info("Exiting handleDeleteFirestation method");
            return ResponseEntity.ok().body("Firestation successfully deleted");
        } catch (Exception e) {
            logger.error("Error occurred while deleting firestation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        logger.error("Validation error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

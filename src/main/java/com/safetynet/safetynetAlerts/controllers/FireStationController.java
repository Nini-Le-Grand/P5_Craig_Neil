package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
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

/**
 * Controller class for handling {@link Firestation} related HTTP requests.
 */
@RestController
@Validated
public class FireStationController {
    @Autowired
    private FirestationService firestationService;

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    /**
     * Handles the POST request to save a new {@link Firestation}.
     *
     * @param firestation The firestation object to be saved.
     * @param result      The binding result containing validation errors, if any.
     * @return ResponseEntity indicating the status of the operation.
     */
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

    /**
     * Handles the PUT request to edit an existing {@link Firestation}.
     *
     * @param firestation The updated firestation object.
     * @param result      The binding result containing validation errors, if any.
     * @return ResponseEntity indicating the status of the operation.
     */
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

    /**
     * Handles the DELETE request to delete a {@link Firestation}.
     *
     * @param addressOrStation The address or station identifier of the firestation to be deleted.
     * @return ResponseEntity indicating the status of the operation.
     */
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

    /**
     * Exception handler for ConstraintViolationException.
     *
     * @param e The ConstraintViolationException.
     * @return ResponseEntity containing the error message.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        logger.error("Validation error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

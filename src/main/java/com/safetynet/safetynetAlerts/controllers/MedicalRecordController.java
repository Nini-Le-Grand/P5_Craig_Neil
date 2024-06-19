package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.models.PersonInfoDTO;
import com.safetynet.safetynetAlerts.services.MedicalRecordService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling {@link MedicalRecord} related HTTP requests.
 */
@RestController
@Validated
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    /**
     * Handles the POST request to save a new {@link MedicalRecord}.
     *
     * @param medicalRecord The medical record object to be saved.
     * @param result        The binding result containing validation errors, if any.
     * @return ResponseEntity indicating the status of the operation.
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<String> handlePostMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord,
                                                          BindingResult result) {
        logger.info("Entered handlePostMedicalRecord method");
        logger.info("Request to save medical record: {}", medicalRecord);
        try {
            medicalRecordService.saveMedicalRecord(medicalRecord);
            logger.info("Medical record successfully saved: {}", medicalRecord);
            logger.info("Exiting handlePostMedicalRecord method");
            return ResponseEntity.status(HttpStatus.CREATED).body("Medical record successfully saved");
        } catch (Exception e) {
            logger.error("Error occurred while saving medical record: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Handles the PUT request to edit an existing {@link MedicalRecord}.
     *
     * @param medicalRecordDTO The DTO containing the updated medical record information.
     * @param result           The binding result containing validation errors, if any.
     * @return ResponseEntity indicating the status of the operation.
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<String> handlePutMedicalRecord(@RequestBody @Valid MedicalRecordUpdateDTO medicalRecordDTO,
                                                         BindingResult result) {
        logger.info("Entered handlePutMedicalRecord method");
        logger.info("Request to edit medical record: {}", medicalRecordDTO);
        try {
            medicalRecordService.editMedicalRecord(medicalRecordDTO);
            logger.info("Medical record successfully edited: {}", medicalRecordDTO);
            logger.info("Exiting handlePutMedicalRecord method");
            return ResponseEntity.status(HttpStatus.OK).body("Medical record successfully edited");
        } catch (Exception e) {
            logger.error("Error occurred while editing medical record: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handles the DELETE request to delete a {@link MedicalRecord}.
     *
     * @param idPersonDTO The DTO containing the person identifier (first name and last name).
     * @param result      The binding result containing validation errors, if any.
     * @return ResponseEntity indicating the status of the operation.
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<String> handleDeleteMedicalRecord(@RequestBody @Valid PersonIdDTO idPersonDTO,
                                                            BindingResult result) {
        logger.info("Entered handleDeleteMedicalRecord method");
        logger.info("Request to delete medical record: {}", idPersonDTO);
        try {
            logger.info("Medical record successfully deleted: {}", idPersonDTO);
            logger.info("Exiting handleDeleteMedicalRecord method");
            medicalRecordService.deleteMedicalRecord(idPersonDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Medical record successfully deleted");
        } catch (Exception e) {
            logger.error("Error occurred while deleting medical record: {}", e.getMessage());
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


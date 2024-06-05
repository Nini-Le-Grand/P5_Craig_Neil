package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
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

@RestController
@Validated
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);


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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        logger.error("Validation error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


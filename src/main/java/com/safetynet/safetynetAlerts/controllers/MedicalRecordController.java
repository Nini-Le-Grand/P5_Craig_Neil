package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.MedicalRecordUpdateDTO;
import com.safetynet.safetynetAlerts.exceptions.ConflictException;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import com.safetynet.safetynetAlerts.services.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping("/medicalRecord")
    public ResponseEntity<String> handlePostMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                medicalRecordService.saveMedicalRecord(medicalRecord);
                return ResponseEntity.status(HttpStatus.CREATED).body("Medical record successfully saved");
            } catch (ConflictException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }


    @PutMapping("/medicalRecord")
    public ResponseEntity<String> handlePutMedicalRecord(@RequestBody @Valid MedicalRecordUpdateDTO medicalRecordDTO, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                medicalRecordService.editMedicalRecord(medicalRecordDTO);
                return ResponseEntity.status(HttpStatus.OK).body("Medical record successfully updated");
            } catch (NotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }


    @DeleteMapping("/medicalRecord")
    public ResponseEntity<String> handleDeleteMedicalRecord(@RequestBody @Valid PersonIdDTO idPersonDTO, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                medicalRecordService.deleteMedicalRecord(idPersonDTO);
                return ResponseEntity.status(HttpStatus.OK).body("Medical record successfully deleted");
            } catch (NotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}


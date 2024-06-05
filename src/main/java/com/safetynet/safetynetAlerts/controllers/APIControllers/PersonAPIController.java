package com.safetynet.safetynetAlerts.controllers.APIControllers;

import com.safetynet.safetynetAlerts.models.APIDTOs.PersonInfoDTO;
import com.safetynet.safetynetAlerts.services.APIServices.PersonAPIService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class PersonAPIController {
    @Autowired
    private PersonAPIService personAPIService;

    @GetMapping("/childAlert")
    public ResponseEntity<?> handleGetFirestation(@RequestParam @NotBlank String address) {
        try {
            Object dto = personAPIService.processChildAlert(address);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam("firstName") @NotBlank String firstName, @RequestParam(
            "lastName") @NotBlank String lastName) {
        try {
            List<PersonInfoDTO> personDto = personAPIService.processPersonInfo(firstName, lastName);
            return ResponseEntity.status(HttpStatus.OK).body(personDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmail(@RequestParam("city") @NotBlank String city) {
        try {
            List<String> emails = personAPIService.processCommunityEmail(city);
            return ResponseEntity.status(HttpStatus.OK).body(emails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

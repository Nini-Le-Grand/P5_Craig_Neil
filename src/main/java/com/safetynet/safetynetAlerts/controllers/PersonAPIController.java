package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.PersonInfoDTO;
import com.safetynet.safetynetAlerts.services.PersonAPIService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for handling API requests related to persons.
 */
@RestController
@Validated
public class PersonAPIController {
    @Autowired
    private PersonAPIService personAPIService;

    private static final Logger logger = LoggerFactory.getLogger(PersonAPIController.class);

    /**
     * Handles the GET request to retrieve {@link com.safetynet.safetynetAlerts.models.ChildAlertDTO} information for a given address.
     *
     * @param address The address for which child alert information is requested.
     * @return ResponseEntity containing the child alert information or an error message.
     */
    @GetMapping("/childAlert")
    public ResponseEntity<?> handleGetChildAlert(@RequestParam("address") @NotBlank String address) {
        logger.info("Entered handleGetChildAlert method");
        logger.info("Request to get childAlert: {}", address);
        try {
        Object childAlertDto = personAPIService.processChildAlert(address);
            logger.info("ChildAlert successfully retrieved: {}", childAlertDto);
            logger.info("Exiting handleGetChildAlert method");
            return ResponseEntity.status(HttpStatus.OK).body(childAlertDto);
        } catch (Exception e) {
            logger.error("Error occurred while getting childAlert: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handles the GET request to retrieve {@link PersonInfoDTO} for a person identified by first name and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     * @return ResponseEntity containing the personal information or an error message.
     */
    @GetMapping("/personInfo")
    public ResponseEntity<?> handleGetPersonInfo(@RequestParam("firstName") @NotBlank String firstName, @RequestParam("lastName") @NotBlank String lastName) {
        logger.info("Entered handleGetPersonInfo method");
        logger.info("Request to get personInfo: {}", firstName + " " + lastName);
        try {
            List<PersonInfoDTO> personInfoDto = personAPIService.processPersonInfo(firstName, lastName);
            logger.info("PersonInfo successfully retrieved: {}", personInfoDto);
            logger.info("Exiting handleGetPersonInfo method");
            return ResponseEntity.status(HttpStatus.OK).body(personInfoDto);
        } catch (Exception e) {
            logger.error("Error occurred while getting personInfo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handles the GET request to retrieve email addresses of all persons living in a given city.
     *
     * @param city The city for which email addresses are requested.
     * @return ResponseEntity containing the email addresses or an error message.
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<?> handleGetCommunityEmails(@RequestParam("city") @NotBlank String city) {
        logger.info("Entered handleGetCommunityEmails method");
        logger.info("Request to get communityEmails: {}", city);
        try {
            List<String> emails = personAPIService.processCommunityEmail(city);
            logger.info("CommunityEmails successfully retrieved: {}", emails);
            logger.info("Exiting handleGetCommunityEmails method");
            return ResponseEntity.status(HttpStatus.OK).body(emails);
        } catch (Exception e) {
            logger.error("Error occurred while getting communityEmails: {}", e.getMessage());
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

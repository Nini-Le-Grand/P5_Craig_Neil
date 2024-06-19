package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.controllers.PersonController;
import com.safetynet.safetynetAlerts.models.FireDTO;
import com.safetynet.safetynetAlerts.models.FirestationDTO;
import com.safetynet.safetynetAlerts.models.FloodDTO;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.FirestationAPIService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

import java.util.Arrays;
import java.util.List;

/**
 * Controller class for handling API requests related to firestations.
 */
@RestController
@Validated
public class FirestationAPIController {
    @Autowired
    private FirestationAPIService firestationAPIService;

    private static final Logger logger = LoggerFactory.getLogger(FirestationAPIController.class);

    /**
     * Handles the GET request to retrieve {@link FirestationDTO} information for a given station number.
     *
     * @param stationNumber The station number for which firestation information is requested.
     * @return ResponseEntity containing the firestation information or an error message.
     */
    @GetMapping("/firestation")
    public ResponseEntity<?> handleGetFirestation(@RequestParam("stationNumber") @NotBlank String stationNumber) {
        logger.info("Entered handleGetFirestation method");
        logger.info("Request to get firestation: {}", stationNumber);
        try {
            FirestationDTO firestationDto = firestationAPIService.processFirestation(stationNumber);
            logger.info("Firestation successfully retrieved: {}", firestationDto);
            logger.info("Exiting handleGetFirestation method");
            return ResponseEntity.status(HttpStatus.OK).body(firestationDto);
        } catch (Exception e) {
            logger.error("Error occurred while getting firestation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handles the GET request to retrieve phone alert information for a given firestation.
     *
     * @param firestation The firestation for which phone alert information is requested.
     * @return ResponseEntity containing the phone alert information or an error message.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<?> handleGetPhoneAlert(@RequestParam("firestation") @NotBlank String firestation) {
        logger.info("Entered handleGetPhoneAlert method");
        logger.info("Request to get phoneAlert: {}", firestation);
        try {
            List<String> phones = firestationAPIService.processPhoneAlert(firestation);
            logger.info("PhoneAlert successfully retrieved: {}", phones);
            logger.info("Exiting handleGetPhoneAlert method");
            return ResponseEntity.status(HttpStatus.OK).body(phones);
        } catch (Exception e) {
            logger.error("Error occurred while getting phoneAlert: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handles the GET request to retrieve {@link FireDTO} information for a given address.
     *
     * @param address The address for which fire information is requested.
     * @return ResponseEntity containing the fire information or an error message.
     */
    @GetMapping("/fire")
    public ResponseEntity<?> handleGetFire(@RequestParam("address") @NotBlank String address) {
        logger.info("Entered handleGetFire method");
        logger.info("Request to get fire: {}", address);
        try {
            FireDTO fireDto = firestationAPIService.processFire(address);
            logger.info("Fire successfully retrieved: {}", fireDto);
            logger.info("Exiting handleGetFire method");
            return ResponseEntity.status(HttpStatus.OK).body(fireDto);
        } catch (Exception e) {
            logger.error("Error occurred while getting fire: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handles the GET request to retrieve {@link FloodDTO} information for a list of fire stations.
     *
     * @param stations The list of fire stations for which flood information is requested.
     * @return ResponseEntity containing the flood information or an error message.
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<?> handleGetFlood(@RequestParam("stations") @NotNull @NotEmpty String[] stations) {
        logger.info("Entered handleGetFlood method");
        logger.info("Request to get flood: {}", Arrays.toString(stations));
        try {
            List<FloodDTO> floodDTOS = firestationAPIService.processFlood(stations);
            logger.info("Flood successfully retrieved: {}", floodDTOS);
            logger.info("Exiting handleGetFlood method");
            return ResponseEntity.status(HttpStatus.OK).body(floodDTOS);
        } catch (Exception e) {
            logger.error("Error occurred while getting flood: {}", e.getMessage());
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

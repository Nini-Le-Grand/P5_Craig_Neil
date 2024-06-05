package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.PersonService;
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
public class PersonController {
    @Autowired
    private PersonService personService;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @PostMapping("/person")
    public ResponseEntity<String> handlePostPerson(@RequestBody @Valid Person person, BindingResult result) {
        logger.info("Entered handlePostPerson method");
        logger.info("Request to save person: {}", person);
        try {
            personService.savePerson(person);
            logger.info("Person successfully saved: {}", person);
            logger.info("Exiting handlePostPerson method");
            return ResponseEntity.status(HttpStatus.CREATED).body("Person successfully saved");
        } catch (Exception e) {
            logger.error("Error occurred while saving person: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/person")
    public ResponseEntity<String> handlePutPerson(@RequestBody @Valid PersonUpdateDTO personDTO, BindingResult result) {
        logger.info("Entered handlePutPerson method");
        logger.info("Request to edit person: {}", personDTO);
        try {
            personService.editPerson(personDTO);
            logger.info("Person successfully edited: {}", personDTO);
            logger.info("Exiting handlePutPerson method");
            return ResponseEntity.ok().body("Person successfully edited");
        } catch (Exception e) {
            logger.error("Error occurred while editing person: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> handleDeletePerson(@RequestBody @Valid PersonIdDTO personDTO, BindingResult result) {
        logger.info("Entered handleDeletePerson method");
        logger.info("Request to delete person: {}", personDTO);
        try {
            personService.deletePerson(personDTO);
            logger.info("Person successfully deleted: {}", personDTO);
            logger.info("Exiting handleDeletePerson method");
            return ResponseEntity.status(HttpStatus.OK).body("Person successfully deleted");
        } catch (Exception e) {
            logger.error("Error occurred while deleting person: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        logger.error("Validation error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

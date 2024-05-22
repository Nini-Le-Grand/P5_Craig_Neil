package com.safetynet.safetynetAlerts.controllers;

import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import com.safetynet.safetynetAlerts.exceptions.ConflictException;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping("/person")
    public ResponseEntity<String> handlePostPerson(@RequestBody @Valid Person person, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                personService.savePerson(person);
                return ResponseEntity.status(HttpStatus.CREATED).body("Person successfully saved");
            } catch (ConflictException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PutMapping("/person")
    public ResponseEntity<String> handlePutPerson(@RequestBody @Valid PersonUpdateDTO personDTO, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                personService.editPerson(personDTO);
                return ResponseEntity.ok().body("Person successfully updated");
            } catch (NotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> handleDeletePerson(@RequestBody @Valid PersonIdDTO personDTO, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                personService.deletePerson(personDTO);
                return ResponseEntity.status(HttpStatus.OK).body("Person successfully deleted");
            } catch (NotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}

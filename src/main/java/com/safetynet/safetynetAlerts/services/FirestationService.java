package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.exceptions.ConflictException;
import com.safetynet.safetynetAlerts.exceptions.NotFoundException;
import com.safetynet.safetynetAlerts.models.Firestation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirestationService {

    @Autowired
    private FirestationDAO firestationDAO;

    public void saveFirestation(Firestation firestation) throws ConflictException {
        if (firestationDAO.findFirestation(firestation.getAddress()).isEmpty()) {
            firestationDAO.addFirestation(firestation);
        } else {
            throw new ConflictException("Firestation already saved in db");
        }
    }

    public void editFirestation(Firestation firestation) throws NotFoundException {
        Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(firestation.getAddress());
        if (optionalFirestation.isPresent()) {
            firestationDAO.setFirestation(firestation, optionalFirestation.get());
        } else {
            throw new NotFoundException("Cannot find firestation to edit");
        }
    }

    public void deleteFirestation(String addressOrStation) throws NotFoundException {
        if (!StringUtils.isNumeric(addressOrStation)) {
            Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(addressOrStation);
            if (optionalFirestation.isPresent()) {
                firestationDAO.removeFirestation(addressOrStation, "address");
            } else {
                throw new NotFoundException("Cannot find firestation to delete");
            }
        } else {
            List<Firestation> collectedFirestations = firestationDAO.collectFirestation(addressOrStation);
            if (!collectedFirestations.isEmpty()) {
                firestationDAO.removeFirestation(addressOrStation, "station");
            } else {
                throw new NotFoundException("Cannot find firestations to delete");
            }
        }
    }
}

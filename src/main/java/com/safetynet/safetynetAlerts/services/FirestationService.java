package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.Person;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to {@link Firestation} entities.
 */
@Setter
@Service
public class FirestationService {

    @Autowired
    private FirestationDAO firestationDAO;

    /**
     * Saves a new {@link Firestation} entity to the data set.
     *
     * @param firestation the Firestation entity to save
     * @throws Exception if the firestation already exists in the data set
     */
    public void saveFirestation(Firestation firestation) throws Exception {
        if (firestationDAO.findFirestation(firestation.getAddress()).isEmpty()) {
            firestationDAO.addFirestation(firestation);
        } else {
            throw new Exception("Firestation already saved");
        }
    }

    /**
     * Edits an existing {@link Firestation} entity in the data set.
     *
     * @param firestation the Firestation entity containing updated information
     * @throws Exception if the firestation does not exist in the data set
     */
    public void editFirestation(Firestation firestation) throws Exception {
        Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(firestation.getAddress());
        if (optionalFirestation.isPresent()) {
            firestationDAO.setFirestation(firestation, optionalFirestation.get());
        } else {
            throw new Exception("Cannot find firestation to edit");
        }
    }

    /**
     * Deletes an existing {@link Firestation} entity from the data set.
     *
     * @param addressOrStation the address or station number of the firestation to delete
     * @throws Exception if the firestation does not exist in the data set
     */
    public void deleteFirestation(String addressOrStation) throws Exception {
        if (StringUtils.isNumeric(addressOrStation)) {
            List<Firestation> collectedFirestations = firestationDAO.collectFirestation(addressOrStation);
            if (!collectedFirestations.isEmpty()) {
                firestationDAO.removeFirestation(addressOrStation, "station");
            } else {
                throw new Exception("Cannot find firestation to delete");
            }
        } else {
            Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(addressOrStation);
            if (optionalFirestation.isPresent()) {
                firestationDAO.removeFirestation(addressOrStation, "address");
            } else {
                throw new Exception("Cannot find address to delete");
            }
        }
    }
}

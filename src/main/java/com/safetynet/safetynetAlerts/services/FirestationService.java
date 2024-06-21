package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.Person;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    /**
     * Saves a new {@link Firestation} entity to the data set.
     *
     * @param firestation the Firestation entity to save
     * @throws Exception if the firestation already exists in the data set
     */
    public void saveFirestation(Firestation firestation) throws Exception {
        logger.info("Checking if Firestation exists in data set");
        if (firestationDAO.findFirestation(firestation.getAddress()).isEmpty()) {
            firestationDAO.addFirestation(firestation);
        } else {
            logger.error("Firestation already exists in data set");
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
        logger.info("Checking if Firestation exists in data set");
        Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(firestation.getAddress());
        if (optionalFirestation.isPresent()) {
            firestationDAO.setFirestation(firestation, optionalFirestation.get());
        } else {
            logger.error("Cannot find Firestation in data set");
            throw new Exception("Cannot find Firestation to edit");
        }
    }

    /**
     * Deletes an existing {@link Firestation} entity from the data set.
     *
     * @param addressOrStation the address or station number of the firestation to delete
     * @throws Exception if the firestation does not exist in the data set
     */
    public void deleteFirestation(String addressOrStation) throws Exception {
        logger.info("Checking if Firestations should be deleted on address or station");
        if (StringUtils.isNumeric(addressOrStation)) {
            logger.info("Entering Firestation deletion on station");
            logger.info("Checking if station exists in data set");
            List<Firestation> collectedFirestations = firestationDAO.collectFirestation(addressOrStation);
            if (!collectedFirestations.isEmpty()) {
                firestationDAO.removeFirestation(addressOrStation, "station");
            } else {
                logger.error("Cannot find station in data set");
                throw new Exception("Cannot find station to delete");
            }
        } else {
            logger.info("Entering Firestation deletion on address");
            logger.info("Checking if address exists in data set");
            Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(addressOrStation);
            if (optionalFirestation.isPresent()) {
                firestationDAO.removeFirestation(addressOrStation, "address");
            } else {
                logger.error("Cannot find address in data set");
                throw new Exception("Cannot find address to delete");
            }
        }
    }
}

package com.safetynet.safetynetAlerts.services;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.models.Firestation;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Service
public class FirestationService {

    @Autowired
    private FirestationDAO firestationDAO;

    public void saveFirestation(Firestation firestation) throws Exception {
        if (firestationDAO.findFirestation(firestation.getAddress()).isEmpty()) {
            firestationDAO.addFirestation(firestation);
        } else {
            throw new Exception("Firestation already saved");
        }
    }

    public void editFirestation(Firestation firestation) throws Exception {
        Optional<Firestation> optionalFirestation = firestationDAO.findFirestation(firestation.getAddress());
        if (optionalFirestation.isPresent()) {
            firestationDAO.setFirestation(firestation, optionalFirestation.get());
        } else {
            throw new Exception("Cannot find firestation to edit");
        }
    }

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

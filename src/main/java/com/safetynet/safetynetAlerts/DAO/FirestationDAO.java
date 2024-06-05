package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FirestationDAO {
    @Autowired
    private JSONDataDAO jsonDataDAO;

    public List<Firestation> getFirestations() {
        return jsonDataDAO.getJsonData().getFirestations();
    }

    public void addFirestation(Firestation firestation) {
        getFirestations().add(firestation);
    }

    public void setFirestation(Firestation firestation, Firestation firestationToEdit) {
        firestationToEdit.setStation(firestation.getStation());
    }

    public void removeFirestation(String value, String field) {
        if (field.equals("address")) {
            getFirestations().removeIf(firestation -> firestation.getAddress().equals(value));
        } else {
            getFirestations().removeIf(firestation -> firestation.getStation().equals(value));
        }
    }

    public Optional<Firestation> findFirestation(String address) {
        return getFirestations().stream().filter(firestation -> firestation.getAddress().equals(address)).findAny();
    }

    public List<Firestation> collectFirestation(String station) {
        return getFirestations().stream().filter(firestation -> firestation.getStation().equals(station)).collect(Collectors.toList());
    }
}

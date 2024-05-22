package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        switch (field) {
            case "address":
                getFirestations().removeIf(firestation -> Objects.equals(firestation.getAddress(), value));
            case "station":
                getFirestations().removeIf(firestation -> Objects.equals(firestation.getStation(), value));
        }
    }

    public Optional<Firestation> findFirestation(String address) {
        return getFirestations()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .findAny();
    }

    public List<Firestation> collectFirestation(String station) {
        return getFirestations()
                .stream()
                .filter(firestation -> firestation.getStation().equals(station))
                .collect(Collectors.toList());
    }
}

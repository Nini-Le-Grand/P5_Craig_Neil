package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.data.JSONDataLoader;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object (DAO) for managing {@link Firestation} java object.
 */
@Service
public class FirestationDAO {
    @Autowired
    private JSONDataLoader jsonDataLoader;

    /**
     * Retrieves all {@link Firestation} from the data set.
     *
     * @return The list of firestations.
     */
    public List<Firestation> getFirestations() {
        return jsonDataLoader.getJsonData().getFirestations();
    }

    /**
     * Adds a new {@link Firestation} to the data set.
     *
     * @param firestation the firestation to be added.
     */
    public void addFirestation(Firestation firestation) {
        getFirestations().add(firestation);
    }

    /**
     * Updates the details of an existing {@link Firestation}.
     *
     * @param firestation       The updated firestation.
     * @param firestationToEdit The firestation to update.
     */
    public void setFirestation(Firestation firestation, Firestation firestationToEdit) {
        firestationToEdit.setStation(firestation.getStation());
    }

    /**
     * Removes a {@link Firestation} from the data set.
     *
     * @param value The value to match (either address or station).
     * @param field The field indicating whether to match by address or station.
     */
    public void removeFirestation(String value, String field) {
        if (field.equals("address")) {
            getFirestations().removeIf(firestation -> firestation.getAddress().equals(value));
        } else {
            getFirestations().removeIf(firestation -> firestation.getStation().equals(value));
        }
    }

    /**
     * Finds a {@link Firestation} by the first and last name of the person.
     *
     * @param address The address of the firestation to find.
     * @return An Optional containing the firestation if found, otherwise empty.
     */
    public Optional<Firestation> findFirestation(String address) {
        return getFirestations().stream().filter(firestation -> firestation.getAddress().equals(address)).findAny();
    }

    /**
     * Collects all {@link Firestation} with a given station number.
     *
     * @param station The station number to filter by.
     * @return The list of firestations with the specified station number.
     */
    public List<Firestation> collectFirestation(String station) {
        return getFirestations().stream().filter(firestation -> firestation.getStation().equals(station)).collect(Collectors.toList());
    }
}

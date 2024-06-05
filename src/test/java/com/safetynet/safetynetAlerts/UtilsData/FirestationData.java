package com.safetynet.safetynetAlerts.UtilsData;

import com.safetynet.safetynetAlerts.models.Firestation;

import java.util.ArrayList;
import java.util.List;

public class FirestationData {
    public Firestation getFireStation() {
        Firestation firestation = new Firestation();

        firestation.setStation("5");
        firestation.setAddress("10 Downing Street");

        return firestation;
    }

    public Firestation getFireStationUpdteDTO() {
        Firestation firestation = new Firestation();

        firestation.setStation("6");
        firestation.setAddress("10 Downing Street");

        return firestation;
    }

    public List<Firestation> getListOfFirestation() {
        Firestation firestation1 = new Firestation();
        firestation1.setStation("1");
        firestation1.setAddress("10 Downing Street");

        Firestation firestation2 = new Firestation();
        firestation2.setStation("1");
        firestation2.setAddress("12 Downing Street");

        List<Firestation> firestations = new ArrayList<>();
        firestations.add(firestation1);
        firestations.add(firestation2);

        return firestations;
    }
}

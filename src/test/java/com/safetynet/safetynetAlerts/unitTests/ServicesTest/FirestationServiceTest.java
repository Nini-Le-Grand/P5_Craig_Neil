package com.safetynet.safetynetAlerts.unitTests.ServicesTest;

import com.safetynet.safetynetAlerts.DAO.FirestationDAO;
import com.safetynet.safetynetAlerts.UtilsData.FirestationData;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.services.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FirestationServiceTest {
    @Mock
    private FirestationDAO firestationDAO;

    @InjectMocks
    private FirestationService firestationService;

    @InjectMocks
    private FirestationData firestationData;

    @BeforeEach
    void before() {
        firestationService.setFirestationDAO(firestationDAO);
    }

    @Test
    void testSaveFirestationSuccess() throws Exception {
        Firestation firestation = firestationData.getFireStation();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.empty());

        firestationService.saveFirestation(firestation);
        verify(firestationDAO, times(1)).addFirestation(firestation);
    }

    @Test
    void testSaveFirestationFailure() {
        Firestation firestation = firestationData.getFireStation();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.of(firestation));

        Exception exception = assertThrows(Exception.class, () -> firestationService.saveFirestation(firestation));
        assertEquals(exception.getMessage(), "Firestation already saved");
    }

    @Test
    void testEditFirestationSuccess() throws Exception {
        Firestation firestation = firestationData.getFireStation();
        Firestation firestationUpdateDTO = firestationData.getFireStationUpdteDTO();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.of(firestation));

        firestationService.editFirestation(firestationUpdateDTO);
        verify(firestationDAO, times(1)).setFirestation(firestationUpdateDTO, firestation);
    }

    @Test
    void testEditFirestationFailure() {
        Firestation firestationUpdateDTO = firestationData.getFireStationUpdteDTO();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class,
                () -> firestationService.editFirestation(firestationUpdateDTO));
        assertEquals(exception.getMessage(), "Cannot find Firestation to edit");
    }

    @Test
    void testDeleteFirestationByStationSuccess() throws Exception {
        Firestation firestation = firestationData.getFireStation();
        List<Firestation> firestations = List.of(firestation);

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(firestations);

        firestationService.deleteFirestation(firestation.getStation());
        verify(firestationDAO, times(1)).removeFirestation(firestation.getStation(), "station");
    }

    @Test
    void testDeleteFirestationStationFailure() {
        Firestation firestation = firestationData.getFireStation();

        when(firestationDAO.collectFirestation(anyString()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class,
                () -> firestationService.deleteFirestation(firestation.getStation()));
        assertEquals(exception.getMessage(), "Cannot find station to delete");
    }

    @Test
    void testDeleteFirestationByAddressSuccess() throws Exception {
        Firestation firestation = firestationData.getFireStation();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.of(firestation));

        firestationService.deleteFirestation(firestation.getAddress());
        verify(firestationDAO, times(1)).removeFirestation(firestation.getAddress(), "address");
    }

    @Test
    void testDeleteFirestationAddressFailure() {
        Firestation firestation = firestationData.getFireStation();

        when(firestationDAO.findFirestation(anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class,
                () -> firestationService.deleteFirestation(firestation.getAddress()));
        assertEquals(exception.getMessage(), "Cannot find address to delete");
    }
}

package com.musalasoft.dronesadministration.drone.adapters;

import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingInputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationInputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DroneComponentTests {

    @Mock
    DroneRegistrationInputBoundary droneRegistrationInputBoundary;

    @Mock
    MedicationLoadingInputBoundary medicationLoadingInputBoundary;

    DroneComponent droneComponent;

    @BeforeEach
    void setUp() {
        droneComponent = new DroneServiceComponent(droneRegistrationInputBoundary, medicationLoadingInputBoundary);
    }

    @Test
    void registerDrone() {
        DroneRegistrationRequest registrationRequest = new DroneRegistrationRequest();
        droneComponent.registerDrone(registrationRequest);
        verify(droneRegistrationInputBoundary, times(1)).registerDrone(registrationRequest);
    }

    @Test
    void updateRegisteredDrone() {
        DroneRegistrationRequest registrationRequest = new DroneRegistrationRequest();
        droneComponent.updateRegisteredDrone(registrationRequest);
        verify(droneRegistrationInputBoundary, times(1)).updateRegisteredDrone(registrationRequest);
    }

    @Test
    void getDroneBySerialNumber() {
        String droneSerialNumber = "ABC123";
        droneComponent.getDroneBySerialNumber(droneSerialNumber);
        verify(droneRegistrationInputBoundary, times(1)).getDroneBySerialNumber(droneSerialNumber);
    }

    @Test
    void deleteDroneBySerialNumber() {
        String droneSerialNumber = "ABC123";
        droneComponent.deleteDroneBySerialNumber(droneSerialNumber);
        verify(droneRegistrationInputBoundary, times(1)).deleteDroneBySerialNumber(droneSerialNumber);
    }

    @Test
    void loadDroneWithMedications() {
        MedicationLoadingRequest medicationLoadingRequest = new MedicationLoadingRequest();
        droneComponent.loadDroneWithMedications(medicationLoadingRequest);
        verify(medicationLoadingInputBoundary, times(1)).loadDroneWithMedications(medicationLoadingRequest);
    }

    @Test
    void getDronesAvailableForLoading() {
        MedicationLoadingRequest medicationLoadingRequest = new MedicationLoadingRequest();
        droneComponent.getDronesAvailableForLoading(medicationLoadingRequest);
        verify(medicationLoadingInputBoundary, times(1)).getDronesAvailableForLoading(medicationLoadingRequest);
    }

    @Test
    void getDronesPage() {
        MedicationLoadingRequest medicationLoadingRequest = new MedicationLoadingRequest();
        droneComponent.getDronesPage(medicationLoadingRequest);
        verify(medicationLoadingInputBoundary, times(1)).getDronesPage(medicationLoadingRequest);
    }
}

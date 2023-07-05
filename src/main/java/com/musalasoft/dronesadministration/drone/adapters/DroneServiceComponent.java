package com.musalasoft.dronesadministration.drone.adapters;

import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingInputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationInputBoundary;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;
import org.springframework.stereotype.Component;

@Component
public class DroneServiceComponent implements DroneComponent {

    private final DroneRegistrationInputBoundary droneRegistrationInputBoundary;
    private final MedicationLoadingInputBoundary medicationLoadingInputBoundary;

    public DroneServiceComponent(DroneRegistrationInputBoundary droneRegistrationInputBoundary,
                                 MedicationLoadingInputBoundary medicationLoadingInputBoundary) {
        this.droneRegistrationInputBoundary = droneRegistrationInputBoundary;
        this.medicationLoadingInputBoundary = medicationLoadingInputBoundary;
    }

    @Override
    public DroneRegistrationResponse registerDrone(DroneRegistrationRequest request) {
        return (DroneRegistrationResponse) droneRegistrationInputBoundary.registerDrone(request);
    }

    @Override
    public DroneRegistrationResponse updateRegisteredDrone(DroneRegistrationRequest request) {
        return (DroneRegistrationResponse) droneRegistrationInputBoundary.updateRegisteredDrone(request);
    }

    @Override
    public DroneRegistrationResponse getDroneBySerialNumber(String serialNumber) {
        return (DroneRegistrationResponse) droneRegistrationInputBoundary.getDroneBySerialNumber(serialNumber);
    }

    @Override
    public DroneRegistrationResponse deleteDroneBySerialNumber(String serialNumber) {
        return (DroneRegistrationResponse) droneRegistrationInputBoundary.deleteDroneBySerialNumber(serialNumber);
    }

    @Override
    public MedicationLoadingResponse loadDroneWithMedications(MedicationLoadingRequest medicationLoadingRequest) {
        return (MedicationLoadingResponse) medicationLoadingInputBoundary.loadDroneWithMedications(medicationLoadingRequest);
    }

    @Override
    public MedicationLoadingResponsePage getDronesAvailableForLoading(MedicationLoadingRequest medicationLoadingRequest) {
        return (MedicationLoadingResponsePage) medicationLoadingInputBoundary.getDronesAvailableForLoading(medicationLoadingRequest);
    }

    @Override
    public MedicationLoadingResponsePage getDronesPage(MedicationLoadingRequest medicationLoadingRequest) {
        return (MedicationLoadingResponsePage) medicationLoadingInputBoundary.getDronesPage(medicationLoadingRequest);
    }
}

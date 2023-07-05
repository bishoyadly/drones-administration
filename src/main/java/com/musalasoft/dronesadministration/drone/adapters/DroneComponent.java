package com.musalasoft.dronesadministration.drone.adapters;

import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;

public interface DroneComponent {
    DroneRegistrationResponse registerDrone(DroneRegistrationRequest request);

    DroneRegistrationResponse updateRegisteredDrone(DroneRegistrationRequest request);

    DroneRegistrationResponse getDroneBySerialNumber(String serialNumber);

    DroneRegistrationResponse deleteDroneBySerialNumber(String serialNumber);

    MedicationLoadingResponse loadDroneWithMedications(MedicationLoadingRequest medicationLoadingRequest);

    MedicationLoadingResponsePage getDronesAvailableForLoading(MedicationLoadingRequest medicationLoadingRequest);

    MedicationLoadingResponsePage getDronesPage(MedicationLoadingRequest medicationLoadingRequest);
}

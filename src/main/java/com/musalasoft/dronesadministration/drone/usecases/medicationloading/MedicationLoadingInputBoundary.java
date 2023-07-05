package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

public interface MedicationLoadingInputBoundary {
    Object loadDroneWithMedications(MedicationLoadingRequest request);

    Object getDronesAvailableForLoading(MedicationLoadingRequest request);

    Object getDronesPage(MedicationLoadingRequest request);
}

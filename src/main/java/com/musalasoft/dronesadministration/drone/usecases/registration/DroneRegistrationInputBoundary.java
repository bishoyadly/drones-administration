package com.musalasoft.dronesadministration.drone.usecases.registration;

public interface DroneRegistrationInputBoundary {
    Object registerDrone(DroneRegistrationRequest request);

    Object updateRegisteredDrone(DroneRegistrationRequest request);

    Object getDroneBySerialNumber(String serialNumber);

    Object deleteDroneBySerialNumber(String serialNumber);
}

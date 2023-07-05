package com.musalasoft.dronesadministration.drone.usecases.registration;

public interface DroneRegistrationGateway {
    boolean droneExistsBySerialNumber(String serialNumber);

    void saveDroneRecord(DroneRegistrationGatewayRequest gatewayRequest);

    DroneRegistrationGatewayResponse getDroneRecordBySerialNumber(String serialNumber);

    void deleteDroneRecordBySerialNumber(String serialNumber);
}

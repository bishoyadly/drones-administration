package com.musalasoft.dronesadministration.drone.adapters.registration;

import com.musalasoft.dronesadministration.drone.adapters.DroneRecord;
import com.musalasoft.dronesadministration.drone.adapters.DroneRecordMapper;
import com.musalasoft.dronesadministration.drone.adapters.DroneRepository;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGateway;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGatewayRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGatewayResponse;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

@Component
public class DatabaseDroneRegistrationGateway implements DroneRegistrationGateway {

    private final DroneRepository droneRepository;
    private final DroneRecordMapper droneRecordMapper;

    public DatabaseDroneRegistrationGateway(DroneRepository droneRepository, DroneRecordMapper droneRecordMapper) {
        this.droneRepository = droneRepository;
        this.droneRecordMapper = droneRecordMapper;
    }

    @Override
    public boolean droneExistsBySerialNumber(String serialNumber) {
        return droneRepository.existsById(serialNumber);
    }

    @Override
    public void saveDroneRecord(DroneRegistrationGatewayRequest gatewayRequest) {
        DroneRecord droneRecord = droneRecordMapper.gatewayRequestToDroneRecord(gatewayRequest);
        droneRepository.save(droneRecord);
    }

    @Override
    public DroneRegistrationGatewayResponse getDroneRecordBySerialNumber(String serialNumber) {
        Optional<DroneRecord> optional = droneRepository.findById(serialNumber);
        if (optional.isPresent()) {
            return droneRecordMapper.droneRecordToGatewayResponse(optional.get());
        } else {
            return buildEmptyDroneRegistrationGatewayResponse();
        }
    }

    @Override
    public void deleteDroneRecordBySerialNumber(String serialNumber) {
        droneRepository.deleteById(serialNumber);
    }

    DroneRegistrationGatewayResponse buildEmptyDroneRegistrationGatewayResponse() {
        DroneRegistrationGatewayResponse gatewayResponse = new DroneRegistrationGatewayResponse();
        gatewayResponse.setSerialNumber("");
        gatewayResponse.setModel("");
        gatewayResponse.setLoadWeightLimitInGram(0);
        gatewayResponse.setBatteryCapacityInPercentage(0);
        gatewayResponse.setState("");
        gatewayResponse.setMedicationGatewayResponseList(new LinkedList<>());
        return gatewayResponse;
    }
}

package com.musalasoft.dronesadministration.drone.adapters;

import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingGatewayResponse;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGatewayRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationGatewayResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DroneRecordMapper {

    DroneRecord gatewayRequestToDroneRecord(DroneRegistrationGatewayRequest gatewayRequest);

    @Mapping(source = "medicationRecordList", target = "medicationGatewayResponseList")
    DroneRegistrationGatewayResponse droneRecordToGatewayResponse(DroneRecord droneRecord);


    @Mapping(target = "droneSerialNumber", source = "serialNumber")
    @Mapping(target = "droneModel", source = "model")
    @Mapping(target = "droneLoadWeightLimitInGram", source = "loadWeightLimitInGram")
    @Mapping(target = "droneBatteryCapacityInPercentage", source = "batteryCapacityInPercentage")
    @Mapping(target = "droneState", source = "state")
    @Mapping(target = "medicationGatewayResponseList", source = "medicationRecordList")
    MedicationLoadingGatewayResponse recordToMedicationLoadingGatewayResponse(DroneRecord droneRecord);

    List<MedicationLoadingGatewayResponse> recordListToMedicationLoadingGatewayResponseList(List<DroneRecord> droneRecordList);
}

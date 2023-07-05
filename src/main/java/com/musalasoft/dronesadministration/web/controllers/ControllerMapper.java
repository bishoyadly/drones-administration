package com.musalasoft.dronesadministration.web.controllers;

import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationRequest;
import com.musalasoft.dronesadministration.drone.usecases.registration.DroneRegistrationResponse;
import com.musalasoft.dronesadministration.medication.usecases.MedicationRequest;
import com.musalasoft.dronesadministration.medication.usecases.MedicationResponse;
import com.musalasoft.dronesadministration.model.DroneModelDto;
import com.musalasoft.dronesadministration.model.DronePageDto;
import com.musalasoft.dronesadministration.model.MedicationModelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ControllerMapper {

    MedicationRequest modelDtoToRequest(MedicationModelDto medicationModelDto);

    MedicationModelDto responseToModelDto(MedicationResponse response);

    DroneRegistrationRequest droneRegistrationRequestToDroneModelDto(DroneModelDto droneModelDto);

    @Mapping(target = "medicationModelList", source = "medicationResponseList")
    DroneModelDto droneRegistrationResponseToDroneModelDto(DroneRegistrationResponse registrationResponse);

    @Mapping(target = "serialNumber", source = "droneSerialNumber")
    @Mapping(target = "model", source = "droneModel")
    @Mapping(target = "loadWeightLimitInGram", source = "droneLoadWeightLimitInGram")
    @Mapping(target = "batteryCapacityInPercentage", source = "droneBatteryCapacityInPercentage")
    @Mapping(target = "state", source = "droneState")
    @Mapping(target = "medicationModelList", source = "medicationResponseList")
    DroneModelDto medicationLoadingResponseToDroneModelDto(MedicationLoadingResponse response);

    List<DroneModelDto> medicationLoadingResponseListToDroneModelDtoList(List<MedicationLoadingResponse> response);

    @Mapping(target = "content", source = "medicationLoadingResponseList")
    DronePageDto medicationLoadingResponsePageToDroneModelPage(MedicationLoadingResponsePage responsePage);
}

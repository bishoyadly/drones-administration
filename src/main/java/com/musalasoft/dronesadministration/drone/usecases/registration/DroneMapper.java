package com.musalasoft.dronesadministration.drone.usecases.registration;

import com.musalasoft.dronesadministration.drone.entities.DroneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DroneMapper {
    DroneEntity registrationRequestToDroneEntity(DroneRegistrationRequest droneRegistrationRequest);

    DroneRegistrationGatewayRequest registrationRequestToGatewayRequest(DroneRegistrationRequest request);

    DroneRegistrationResponse registrationRequestToRegistrationResponse(DroneRegistrationRequest request);

    @Mapping(target = "medicationResponseList", expression = "java(new ArrayList())")
    DroneRegistrationResponse droneEntityToRegistrationResponse(DroneEntity droneEntity);

    DroneRegistrationGatewayRequest droneEntityToRegistrationGatewayRequest(DroneEntity droneEntity);

    @Mapping(target = "medicationResponseList", source = "medicationGatewayResponseList")
    DroneRegistrationResponse registrationGatewayResponseToRegistrationResponse(DroneRegistrationGatewayResponse gatewayResponse);

    @Mapping(target = "medicationGatewayResponseList", source = "medicationResponseList")
    DroneRegistrationGatewayResponse registrationResponseToGatewayRegistrationResponse(DroneRegistrationResponse droneRegistrationResponse);
}

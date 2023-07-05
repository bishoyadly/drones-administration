package com.musalasoft.dronesadministration.drone.usecases.medicationloading;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MedicationLoadingMapper {
    @Mapping(target = "droneState", ignore = true)
    MedicationLoadingGatewayRequest requestToGatewayRequest(MedicationLoadingRequest request);

    @Mapping(target = "medicationResponseList", source = "medicationGatewayResponseList")
    MedicationLoadingResponse gatewayResponseToResponse(MedicationLoadingGatewayResponse gatewayResponse);

    List<MedicationLoadingResponse> gatewayResponseListToResponseList(List<MedicationLoadingGatewayResponse> gatewayResponseList);

    @Mapping(target = "medicationLoadingResponseList", source = "medicationLoadingGatewayResponseList")
    MedicationLoadingResponsePage gatewayResponsePageToResponsePage(MedicationLoadingGatewayResponsePage gatewayResponsePage);
}
